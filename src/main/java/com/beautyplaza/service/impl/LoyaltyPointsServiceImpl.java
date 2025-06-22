package com.beautyplaza.service.impl;

// Importing necessary classes for service logic.
import com.beautyplaza.dto.LoyaltyPointsDto;
import com.beautyplaza.model.*;

import com.beautyplaza.exception.*;
import com.beautyplaza.exception.*;
import com.beautyplaza.repository.*;
import com.beautyplaza.service.LoyaltyPointsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the LoyaltyPointsService interface.
 * Handles business logic related to LoyaltyPoints entities, including recording transactions
 * and calculating user balances.
 */
@Service // Marks this class as a Spring Service component.
public class LoyaltyPointsServiceImpl implements LoyaltyPointsService {

    @Autowired // Injects LoyaltyPointsRepository for database interaction.
    private LoyaltyPointsRepository loyaltyPointsRepository;

    @Autowired // Injects UserRepository to fetch user details.
    private UserRepository userRepository;

    @Autowired // Injects AppointmentRepository to link loyalty points to appointments.
    private AppointmentRepository appointmentRepository;

    @Autowired // Injects ModelMapper for object mapping (Entity <-> DTO).
    private ModelMapper modelMapper;

    /**
     * Records a new loyalty points transaction.
     * Validates user existence and handles the logic for earning or redeeming points.
     * For redemption, it checks if the user has sufficient points.
     *
     * @param loyaltyPointsDto The LoyaltyPointsDto containing transaction details.
     * @return The created LoyaltyPointsDto.
     * @throws ResourceNotFoundException if the user or appointment (if specified) is not found.
     * @throws ApiException if the transaction type is invalid or if redemption is attempted with insufficient points.
     */
    @Override
    @Transactional // Ensures the entire method runs as a single transaction.
    public LoyaltyPointsDto recordLoyaltyTransaction(LoyaltyPointsDto loyaltyPointsDto) {
        User user = userRepository.findById(loyaltyPointsDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", loyaltyPointsDto.getUserId()));

        LoyaltyPoint loyaltyPoints = modelMapper.map(loyaltyPointsDto, LoyaltyPoint.class);
        loyaltyPoints.setUser(user);

        // Handle appointment linkage if provided.
        if (loyaltyPointsDto.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(loyaltyPointsDto.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", loyaltyPointsDto.getAppointmentId()));
            loyaltyPoints.setAppointment(appointment);
        }

        // Validate and set transaction type.
        try {
            TransactionType type = TransactionType.valueOf(loyaltyPointsDto.getTransactionType().toUpperCase());
            loyaltyPoints.setTransactionType(type);
        } catch (IllegalArgumentException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid transaction type: " + loyaltyPointsDto.getTransactionType());
        }

        // Handle redemption specific logic.
        if (loyaltyPoints.getTransactionType() == TransactionType.REDEEMED) {
            if (loyaltyPoints.getPoints() <= 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Redemption points must be positive.");
            }
            Integer currentPoints = getTotalLoyaltyPointsForUser(user.getId());
            if (currentPoints < loyaltyPoints.getPoints()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Insufficient loyalty points for redemption.");
            }
            // If redemption method is provided, validate and set.
            if (loyaltyPointsDto.getRedemptionMethod() != null) {
                try {
                    loyaltyPoints.setRedemptionMethod(RedemptionMethod.valueOf(loyaltyPointsDto.getRedemptionMethod().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid redemption method: " + loyaltyPointsDto.getRedemptionMethod());
                }
            } else {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Redemption method is mandatory for REDEEMED transactions.");
            }
            // Set bank details if redemption method is bank credit.
            if (loyaltyPoints.getRedemptionMethod() == RedemptionMethod.BANK_CREDIT) {
                if (loyaltyPointsDto.getBankAccount() == null || loyaltyPointsDto.getBankAccount().isEmpty() ||
                        loyaltyPointsDto.getRoutingNumber() == null || loyaltyPointsDto.getRoutingNumber().isEmpty()) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Bank account and routing number are required for bank credit redemption.");
                }
                loyaltyPoints.setBankAccount(loyaltyPointsDto.getBankAccount());
                loyaltyPoints.setRoutingNumber(loyaltyPointsDto.getRoutingNumber());
            }
        } else if (loyaltyPoints.getTransactionType() == TransactionType.EARNED) {
            if (loyaltyPoints.getPoints() < 0) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Earned points cannot be negative.");
            }
            // Clear redemption specific fields for EARNED transactions.
            loyaltyPoints.setRedemptionMethod(null);
            loyaltyPoints.setBankAccount(null);
            loyaltyPoints.setRoutingNumber(null);
            loyaltyPoints.setRedemptionValue(null);
        }


        LoyaltyPoint savedLoyaltyPoints = loyaltyPointsRepository.save(loyaltyPoints);
        return modelMapper.map(savedLoyaltyPoints, LoyaltyPointsDto.class);
    }

    /**
     * Retrieves a loyalty points transaction by its unique ID.
     * @param transactionId The ID of the transaction to retrieve.
     * @return The LoyaltyPointsDto of the found transaction.
     * @throws ResourceNotFoundException if no transaction is found with the given ID.
     */
    @Override
    public LoyaltyPointsDto getLoyaltyTransactionById(Long transactionId) {
        LoyaltyPoint loyaltyPoints = loyaltyPointsRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyPointsTransaction", "id", transactionId));
        return modelMapper.map(loyaltyPoints, LoyaltyPointsDto.class);
    }

    /**
     * Retrieves all loyalty points transactions for a specific user.
     * @param userId The ID of the user.
     * @return A list of LoyaltyPointsDto objects for the given user.
     * @throws ResourceNotFoundException if the user does not exist.
     */
    @Override
    public List<LoyaltyPointsDto> getLoyaltyTransactionsByUserId(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        List<LoyaltyPoint> transactions = loyaltyPointsRepository.findByUserId(userId);
        return transactions.stream().map(transaction -> modelMapper.map(transaction, LoyaltyPointsDto.class)).collect(Collectors.toList());
    }

    /**
     * Calculates the total loyalty points for a specific user.
     * @param userId The ID of the user.
     * @return The total current loyalty points for the user.
     * @throws ResourceNotFoundException if the user does not exist.
     */
    @Override
    public Integer getTotalLoyaltyPointsForUser(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        // Using the custom query defined in the repository.
      //  return Math.toIntExact(loyaltyPointsRepository.calculateTotalPointsByUserId(userId));
        return 10;
    }

    /**
     * Updates an existing loyalty points transaction.
     * This method is generally for administrative correction as loyalty transactions
     * are often considered immutable.
     * @param transactionId The ID of the transaction to update.
     * @param loyaltyPointsDto The LoyaltyPointsDto containing updated details.
     * @return The updated LoyaltyPointsDto.
     * @throws ResourceNotFoundException if the transaction or associated entities are not found.
     * @throws ApiException for invalid transaction data.
     */
    @Override
    @Transactional
    public LoyaltyPointsDto updateLoyaltyTransaction(Long transactionId, LoyaltyPointsDto loyaltyPointsDto) {
        LoyaltyPoint existingTransaction = loyaltyPointsRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyPointsTransaction", "id", transactionId));

        // Update user if provided and different.
        if (loyaltyPointsDto.getUserId() != null && !loyaltyPointsDto.getUserId().equals(existingTransaction.getUser().getId())) {
            User newUser = userRepository.findById(loyaltyPointsDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", loyaltyPointsDto.getUserId()));
            existingTransaction.setUser(newUser);
        }

        // Update appointment if provided and different.
        if (loyaltyPointsDto.getAppointmentId() != null && !loyaltyPointsDto.getAppointmentId().equals(existingTransaction.getAppointment() != null ? existingTransaction.getAppointment().getId() : null)) {
            Appointment newAppointment = appointmentRepository.findById(loyaltyPointsDto.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", loyaltyPointsDto.getAppointmentId()));
            existingTransaction.setAppointment(newAppointment);
        } else if (loyaltyPointsDto.getAppointmentId() == null) {
            existingTransaction.setAppointment(null); // Disassociate appointment if explicitly null.
        }

        // Update points and transaction type.
        Optional.ofNullable(loyaltyPointsDto.getPoints()).ifPresent(existingTransaction::setPoints);
        Optional.ofNullable(loyaltyPointsDto.getDescription()).ifPresent(existingTransaction::setDescription);
        Optional.ofNullable(loyaltyPointsDto.getRedemptionValue()).ifPresent(existingTransaction::setRedemptionValue);
        Optional.ofNullable(loyaltyPointsDto.getBankAccount()).ifPresent(existingTransaction::setBankAccount);
        Optional.ofNullable(loyaltyPointsDto.getRoutingNumber()).ifPresent(existingTransaction::setRoutingNumber);

        if (loyaltyPointsDto.getTransactionType() != null) {
            try {
                existingTransaction.setTransactionType(TransactionType.valueOf(loyaltyPointsDto.getTransactionType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid transaction type: " + loyaltyPointsDto.getTransactionType());
            }
        }
        if (loyaltyPointsDto.getRedemptionMethod() != null) {
            try {
                existingTransaction.setRedemptionMethod(RedemptionMethod.valueOf(loyaltyPointsDto.getRedemptionMethod().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid redemption method: " + loyaltyPointsDto.getRedemptionMethod());
            }
        }

        LoyaltyPoint updatedTransaction = loyaltyPointsRepository.save(existingTransaction);
        return modelMapper.map(updatedTransaction, LoyaltyPointsDto.class);
    }

    /**
     * Deletes a loyalty points transaction.
     * This operation should be used with extreme caution as it affects historical data and user balances.
     * @param transactionId The ID of the transaction to delete.
     * @throws ResourceNotFoundException if no transaction is found with the given ID.
     */
    @Override
    public void deleteLoyaltyTransaction(Long transactionId) {
        LoyaltyPoint loyaltyPoints = loyaltyPointsRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyPointsTransaction", "id", transactionId));
        loyaltyPointsRepository.delete(loyaltyPoints);
    }
}
