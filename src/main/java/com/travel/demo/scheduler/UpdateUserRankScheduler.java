package com.travel.demo.scheduler;

import com.travel.demo.entity.UserRank;
import com.travel.demo.entity.Users;
import com.travel.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class UpdateUserRankScheduler {
    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 0/15 * * * ?", zone = "Asia/Ho_Chi_Minh")
    public void updateUserRanks() {

        List<Users> users = userRepository.findAll();

        for (Users user : users) {

            if (user.getAccount() == null) continue;

            int point = user.getAccount().getPoint();

            UserRank newRank;

            if (point >= 2000) {
                newRank = UserRank.DIAMOND;
            } else if (point >= 1000) {
                newRank = UserRank.GOLD;
            } else if (point >= 500) {
                newRank = UserRank.SILVER;
            } else {
                newRank = UserRank.BRONZE;
            }

            // chỉ update nếu rank thay đổi
            if (user.getUserRank() != newRank) {
                user.setUserRank(newRank);
                userRepository.save(user);
            }
        }

        System.out.println("✔ Scheduler: User ranks updated by point!");
    }
}
