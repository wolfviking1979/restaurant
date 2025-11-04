package com.restaurant.service;

import com.restaurant.entity.Reservation;
import com.restaurant.entity.RestaurantTable;
import com.restaurant.repository.ReservationRepository;
import com.restaurant.repository.RestaurantTableRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class TableService {

    private final RestaurantTableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    public TableService(RestaurantTableRepository tableRepository,
                        ReservationRepository reservationRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public RestaurantTable createTable(RestaurantTable table) {
        if (tableRepository.existsByTableNumber(table.getTableNumber())) {
            throw new RuntimeException("Table with number " + table.getTableNumber() + " already exists");
        }
        return tableRepository.save(table);
    }

    public List<RestaurantTable> getAllTables() {
        return tableRepository.findAll();
    }

    public List<RestaurantTable> getActiveTables() {
        return tableRepository.findByIsActiveTrue();
    }

    public List<RestaurantTable> getTablesByCapacity(Integer minCapacity) {
        return tableRepository.findByCapacityGreaterThanEqual(minCapacity);
    }

    public List<RestaurantTable> getTablesByType(RestaurantTable.TableType tableType) {
        return tableRepository.findByTableType(tableType);
    }

    public List<RestaurantTable> getAvailableTables(LocalDateTime startTime, LocalDateTime endTime, Integer guestsCount) {
        return tableRepository.findAvailableTables(startTime, endTime, guestsCount);
    }

    public boolean isTableAvailable(Long tableId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(tableId, startTime, endTime);
        return conflicts.isEmpty();
    }

    @Transactional
    public RestaurantTable updateTable(Long id, RestaurantTable tableDetails) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));

        if (!table.getTableNumber().equals(tableDetails.getTableNumber()) &&
                tableRepository.existsByTableNumber(tableDetails.getTableNumber())) {
            throw new RuntimeException("Table with number " + tableDetails.getTableNumber() + " already exists");
        }

        table.setTableNumber(tableDetails.getTableNumber());
        table.setCapacity(tableDetails.getCapacity());
        table.setTableType(tableDetails.getTableType());
        table.setDescription(tableDetails.getDescription());
        table.setIsActive(tableDetails.getIsActive());

        return tableRepository.save(table);
    }

    @Transactional
    public void deactivateTable(Long id) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
        table.setIsActive(false);
        tableRepository.save(table);
    }

    @Transactional
    public void activateTable(Long id) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
        table.setIsActive(true);
        tableRepository.save(table);
    }

    public Map<String, Object> getTableStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTables", tableRepository.count());
        stats.put("activeTables", tableRepository.countActiveTables());
        stats.put("tablesByType", tableRepository.countTablesByType());
        return stats;
    }

    public Optional<RestaurantTable> getTableByNumber(String tableNumber) {
        return tableRepository.findByTableNumber(tableNumber);
    }

    public RestaurantTable getTableById(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
    }
}