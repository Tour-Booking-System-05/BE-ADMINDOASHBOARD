package com.travel.demo.scheduler;

import com.travel.demo.repository.AccountRepository;
import com.travel.demo.service.EmailService;
import com.travel.demo.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminReportScheduler {

    private final AccountRepository accountRepository;
    private final StatisticService statisticService;
    private final EmailService emailService;

    // 08:00 mỗi ngày (giờ VN)
//    @Scheduled(cron = "0 0/1 * * * ?", zone = "Asia/Ho_Chi_Minh")

    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Ho_Chi_Minh")
    public void sendDailyReportToAdminRoleId1() {

        System.out.println("==== mail báo cáo ====");
        var admins = accountRepository.findAdminRoleId1(); // bạn tạo query ở repo
        if (admins.isEmpty()) return;

        int range = 12; // hoặc 1/3/6 tuỳ bạn
        var kpi = statisticService.kpis(range);

        for (var a : admins) {
            emailService.sendStatisticReportEmail(a.getEmail(), kpi, range);
        }
    }
}
