package org.itstep;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Controller
public class AppController {

    static final Logger LOGGER = Logger.getLogger(AppController.class.getName());
    private AtomicLong count = new AtomicLong(0); //Счетчик
    private boolean onceEvent = false; //Однократное событие

    @GetMapping(value = {"", "/"})
    public String index(Model model) {
        model.addAttribute("count", count);
        return "index";
    }

    @GetMapping(value = "/count")
    public String count(Model model) {
        model.addAttribute("count", count);
        return "index::#repeatableEvent";
    }

    @GetMapping(value = "/once")
    public String once(Model model) {
        model.addAttribute("once", onceEvent);
        return "index::#onceEvent";
    }

    @Scheduled(initialDelay = 1000, fixedRate = 3000)
    @Async
    public void refreshParameters() {
        count.incrementAndGet();
        LOGGER.info("repeatable event: "+ LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    //Событие произойдет однократно через 10 с
    @Scheduled(initialDelay = 1000*20, fixedDelay = Long.MAX_VALUE)
    @Async
    public void checkOnceEvent() {
        onceEvent = true;
        LOGGER.info("Once event: "+ LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }
}
