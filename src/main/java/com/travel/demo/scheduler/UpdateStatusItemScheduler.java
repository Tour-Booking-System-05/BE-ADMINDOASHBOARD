package com.travel.demo.scheduler;

import com.travel.demo.entity.Items;
import com.travel.demo.repository.ItemRepository;
import com.travel.demo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class UpdateStatusItemScheduler {

    @Autowired
    private ItemRepository itemRepository;
    @Scheduled(cron = "0 0/15 * * * ?", zone = "Asia/Ho_Chi_Minh")
    public void updateStatus() {
        LocalDate today = LocalDate.now();

        List<Items> list = itemRepository.findByDeletedAtIsNull();
        for (Items item : list) {
            LocalDate start = item.getDateTour();
            LocalDate end = item.getDateEndTour();
            // Bỏ qua nếu tour đã bị hủy thủ công (status = 0)
            if (item.getStatus() != 0 && start != null && end != null) {

                // Nếu hôm nay nằm trong khoảng thời gian tour => "Đang đi"
                if ((today.isEqual(start) || today.isAfter(start)) && today.isBefore(end)) {
                    if (item.getStatus() != 2) {
                        item.setStatus((byte) 2); // Đang đi
                        itemRepository.save(item);
                    }
                }
                // Nếu tour đã kết thúc => "Ẩn" (hoặc kết thúc)
                else if (today.isAfter(end)) {
                    if (item.getStatus() != 0) {
                        item.setStatus((byte) 0); //  kết thúc
                        itemRepository.save(item);
                    }
                }
                // Nếu tour chưa bắt đầu => "Hoạt động"
                else if (today.isBefore(start)) {
                    if (item.getStatus() != 1) {
                        item.setStatus((byte) 1); // Hoạt động
                        itemRepository.save(item);
                    }
                }
            }
        }
    }
}
