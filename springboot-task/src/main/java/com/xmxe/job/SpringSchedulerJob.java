package com.xmxe.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SpringSchedulerJob {

    /**
     * 1、fixedDelay控制方法执行的间隔时间，是以上一次方法执行完开始算起，如上一次方法执行阻塞住了，那么直到上一次执行完，并间隔给定的时间后，执行下一次。
     *
     * 2、fixedRate是按照一定的速率执行，是从上一次方法执行开始的时间算起，如果上一次方法阻塞住了，下一次也是不会执行，但是在阻塞这段时间内累计应该执行的次数，当不再阻塞时，一下子把这些全部执行掉，而后再按照固定速率继续执行。
     *
     * 3、cron表达式可以定制化执行任务，但是执行的方式是与fixedDelay相近的，也是会按照上一次方法结束时间开始算起。
     *
     * 4、initialDelay 。如： @Scheduled(initialDelay = 10000,fixedRate = 15000
     * 这个定时器就是在上一个的基础上加了一个initialDelay = 10000 意思就是在容器启动后,延迟10秒后再执行一次定时器,以后每15秒再执行一次该定时器。
     */

    @Scheduled(cron = "0/10 * * * * ?")
    public void cron(){
        System.out.println("spring task cron 每10s执行一次");
    }

    @Scheduled(fixedDelay = 5000)
    public void fixedDelay(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("spring task fixedDelay(任务执行结束为时间点) 先阻塞10s执行打印 等任务执行完后等待等5s才会执行下一个 此时又阻塞了10s 相当于每隔15s才会打印");
    }
    @Scheduled(initialDelay = 10000, fixedRate = 5000)
    public void fixedRaed(){
        System.out.println("spring task fixedRate(任务执行开始为时间点) initialDelay等待10s后执行 每隔5s执行一次");
    }

}