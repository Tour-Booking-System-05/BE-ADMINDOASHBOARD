package com.travel.demo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.demo.dto.TourDTO;
import com.travel.demo.entity.Categories;
import com.travel.demo.entity.Employees;
import com.travel.demo.entity.Items;
import com.travel.demo.repository.CategoriesRepository;
import com.travel.demo.repository.EmployeesRepository;
import com.travel.demo.repository.ItemRepository;
import com.travel.demo.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoriesRepository categoryRepository;

    @Autowired
    private EmployeesRepository employeeRepository;

    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    @Transactional
    public Page<TourDTO> getAllTour(int page, int size, String[] sort, String keyword) {

        // 1 Xác định hướng sắp xếp
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        // 2Lọc dữ liệu theo từ khóa
        Page<Items> itemsPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            itemsPage = itemRepository.findByTitleTourContainingIgnoreCaseAndDeletedAtIsNull(keyword.trim(), pageable);
        } else {
            itemsPage = itemRepository.findByDeletedAtIsNull(pageable);
        }

        // 3Lấy ngày hôm nay

        // 4 Map Entity → DTO + Cập nhật trạng thái tự động
        return itemsPage.map(item -> {

            // 5Chuyển ảnh JSON → List<String>
            List<String> imgs = new ArrayList<>();
            if (item.getImageUrls() != null && !item.getImageUrls().isEmpty()) {
                try {
                    imgs = mapper.readValue(item.getImageUrls(), new TypeReference<List<String>>() {});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 6 Trả về DTO
            return new TourDTO(
                    item.getItemId(),
                    item.getCategory() != null ? item.getCategory().getCategoryId() : null,
                    item.getGuider() != null ? item.getGuider().getEmployeeId() : null,
                    item.getTitleTour(),
                    item.getDescription(),
                    item.getDateTour(),
                    item.getDateEndTour(),
                    item.getLocated(),
                    item.getVehicle(),
                    item.getComparatingPrice(),
                    item.getDiscount(),
                    item.getPrice(),
                    item.getTotal(),
                    item.getStatus(),
                    imgs
            );
        });
    }

    @Override
    @Transactional
    public TourDTO create(TourDTO tourDTO) {
        Categories category = categoryRepository.findByCategoryIdAndStatusAndDeletedAtIsNull(tourDTO.getCategoryId(), true)
                .orElseThrow(() -> new RuntimeException("Category not found or inactive"));

        Employees guider = employeeRepository.findById(tourDTO.getGuiderId()) .
                orElseThrow(() -> new RuntimeException("Employee not found"));

        Items items = new Items();
        items.setCategory(category);
        items.setGuider(guider);
        items.setTitleTour(tourDTO.getTitleTour());
        items.setDescription(tourDTO.getDescription());
        items.setDateTour(tourDTO.getDateTour());
        items.setDateEndTour(tourDTO.getDateEndTour());
        items.setLocated(tourDTO.getLocated());
        items.setVehicle(tourDTO.getVehicle());
        items.setComparatingPrice(tourDTO.getComparatingPrice());
        items.setDiscount(tourDTO.getDiscount());
        items.setPrice(tourDTO.getPrice());
        items.setTotal(tourDTO.getTotal());
        items.setStatus(tourDTO.getStatus());

        try {
            items.setImageUrls(mapper.writeValueAsString(tourDTO.getImageUrls()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting image URLs to JSON", e);
        }

        itemRepository.save(items);
        tourDTO.setItemId(items.getItemId());
        return tourDTO;
    }

    @Override
    public TourDTO update(Integer id, TourDTO tourDTO) {
        Items item = itemRepository.findByItemIdAndDeletedAtIsNull(id);
        if(item == null) {
            throw new RuntimeException("Không tìm thấy tour để cập nhập");
        }
        // Cập nhật quan hệ
        if (tourDTO.getCategoryId() != null) {
            Categories cate = categoryRepository.findByCategoryIdAndStatusAndDeletedAtIsNull(
                            tourDTO.getCategoryId(), true)
                    .orElseThrow(() -> new EntityNotFoundException("Danh mục không tồn tại hoặc đã bị vô hiệu hóa"));
            item.setCategory(cate);
        }

        if (tourDTO.getGuiderId() != null) {
            Employees guider = employeeRepository.findById(tourDTO.getGuiderId()) .
                    orElseThrow(() -> new EntityNotFoundException("Hướng dẫn viên không tồn tại"));
            item.setGuider(guider); }

        item.setTitleTour(tourDTO.getTitleTour());
        item.setDescription(tourDTO.getDescription());
        item.setDateTour(tourDTO.getDateTour());
        item.setDateEndTour(tourDTO.getDateEndTour());
        item.setLocated(tourDTO.getLocated());
        item.setVehicle(tourDTO.getVehicle());
        item.setComparatingPrice(tourDTO.getComparatingPrice());
        item.setDiscount(tourDTO.getDiscount());
        item.setPrice(tourDTO.getPrice());
        item.setTotal(tourDTO.getTotal());
        item.setStatus(tourDTO.getStatus());
        if (tourDTO.getImageUrls() != null && !tourDTO.getImageUrls().isEmpty()) {
            try {
                item.setImageUrls(mapper.writeValueAsString(tourDTO.getImageUrls()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Lỗi khi xử lý danh sách ảnh", e);
            }
        }
        itemRepository.save(item);
        return tourDTO;
    }

    @Override
    public void softDelete(Integer id) {
        Items item = itemRepository.findByItemIdAndDeletedAtIsNull(id);
        if(item == null) {
            throw new RuntimeException("Không tìm thấy tour để xóa");
        }
        item.setDeletedAt(LocalDate.now());
        itemRepository.save(item);
    }

    @Override
    public TourDTO getById(Integer id) {
        Items item = itemRepository.findByItemIdAndDeletedAtIsNull(id);
        if(item == null) {
            throw new RuntimeException("Không tìm thấy danh mục!");
        }
        // Parse danh sách ảnh từ JSON sang List<String>
        List<String> imageUrls = new ArrayList<>();
        try {
            if(item.getImageUrls() != null && !item.getImageUrls().isEmpty()){
                imageUrls = mapper.readValue(item.getImageUrls(), new TypeReference<List<String>>() {});
            }
        } catch (JsonMappingException e) {
            throw new RuntimeException("Lỗi khi đọc danh sách ảnh", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi khi đọc danh sách ảnh", e);
        }
        return new TourDTO(
                item.getItemId(),
                item.getCategory() != null ? item.getCategory().getCategoryId() : null,
                item.getGuider() != null ? item.getGuider().getEmployeeId() : null,
                item.getTitleTour(),
                item.getDescription(),
                item.getDateTour(),
                item.getDateEndTour(),
                item.getLocated(),
                item.getVehicle(),
                item.getComparatingPrice(),
                item.getDiscount(),
                item.getPrice(),
                item.getTotal(),
                item.getStatus(),
                imageUrls
        );
    }

    // DELETE MULTIPLE
    @Override
    public void deleteMultipe(List<Integer> ids) {
        List<Items> items = itemRepository.findAllById(ids);

        if (items.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy tour nào để xóa");
        }

        // --- Kiểm tra nếu có tour có status = 2 ---
        List<Items> lockedItems = items.stream()
                .filter(i -> i.getStatus() != null && i.getStatus() == 2)
                .collect(Collectors.toList());

        if (!lockedItems.isEmpty()) {
            String lockedNames = lockedItems.stream()
                    .map(Items::getTitleTour)
                    .collect(Collectors.joining(", "));
            throw new IllegalStateException("Không thể xóa tour đang hoạt động (status = 2): " + lockedNames);
        }

        // --- Gắn deletedAt cho các tour hợp lệ ---
        items.forEach(i -> i.setDeletedAt(LocalDate.now()));
        itemRepository.saveAll(items);
    }

    @Override
    @Transactional
    public TourDTO cloneTour(Integer id) {
        // 1Lấy tour gốc
        Items original = itemRepository.findByItemIdAndDeletedAtIsNull(id);
        if (original == null) {
            throw new EntityNotFoundException("Không tìm thấy tour cần sao chép");
        }

        // 2Tạo bản sao
        Items clone = new Items();
        clone.setCategory(original.getCategory());
        clone.setGuider(original.getGuider());
        clone.setTitleTour(original.getTitleTour() + " - Copy");
        clone.setDescription(original.getDescription());
        clone.setLocated(original.getLocated());
        clone.setVehicle(original.getVehicle());
        clone.setComparatingPrice(original.getComparatingPrice());
        clone.setDiscount(original.getDiscount());
        clone.setPrice(original.getPrice());
        clone.setTotal(original.getTotal());
        clone.setImageUrls(original.getImageUrls());
        clone.setDeletedAt(null);

        // 3Nếu tour đang đi (status = 2) → set ngày mới từ ngày mai
        if (original.getStatus() != null ) {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            LocalDate originalStart = original.getDateTour();
            LocalDate originalEnd = original.getDateEndTour();

            long daysBetween = 0;
            if (originalStart != null && originalEnd != null) {
                daysBetween = java.time.temporal.ChronoUnit.DAYS.between(originalStart, originalEnd);
            }

            clone.setDateTour(tomorrow);
            clone.setDateEndTour(tomorrow.plusDays(daysBetween));
        } else {
            clone.setDateTour(original.getDateTour());
            clone.setDateEndTour(original.getDateEndTour());
        }

        // 4Trạng thái tour mới = 0 (đã hủy)
        clone.setStatus((byte) 0);

        // 5 Lưu DB
        Items saved = itemRepository.save(clone);

        // 6Chuyển thành DTO
        List<String> imageList = new ArrayList<>();
        try {
            if (saved.getImageUrls() != null && !saved.getImageUrls().isEmpty()) {
                imageList = mapper.readValue(saved.getImageUrls(), new TypeReference<List<String>>() {});
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new TourDTO(
                saved.getItemId(),
                saved.getCategory() != null ? saved.getCategory().getCategoryId() : null,
                saved.getGuider() != null ? saved.getGuider().getEmployeeId() : null,
                saved.getTitleTour(),
                saved.getDescription(),
                saved.getDateTour(),
                saved.getDateEndTour(),
                saved.getLocated(),
                saved.getVehicle(),
                saved.getComparatingPrice(),
                saved.getDiscount(),
                saved.getPrice(),
                saved.getTotal(),
                saved.getStatus(),
                imageList
        );
    }

}
