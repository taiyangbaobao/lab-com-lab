package limit;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class Limitor {
    // 设定一个限速器
    private static RateLimiter rateLimiter = RateLimiter.create(10);
    private static final int ThreadCount = 100;
    static CountDownLatch countDownLatch = new CountDownLatch(ThreadCount);

    //获取
    public boolean tryAcquire(){
        return Optional.ofNullable(rateLimiter).map(rateLimiter1 -> rateLimiter1.tryAcquire()).orElse(true);
      //  return Optional.ofNullable(rateLimiter).map(RateLimiter::tryAcquire()).orElse(true);
    }

    public static void main(String[] args) throws InterruptedException {

        Runnable runnable = ()->{
            while(true){
                if(rateLimiter.tryAcquire()){
                    System.out.println(Thread.currentThread().getName()+"当前线程获取到了"+","+System.currentTimeMillis());
                    countDownLatch.countDown();
                    break;
                }else{
               //     System.out.println(Thread.currentThread().getName()+"当前线程获未----"+","+System.currentTimeMillis());
                }
            }
        };
        Long start = System.currentTimeMillis();
        for (int i = 0;i<ThreadCount;i++){
            Thread thread = new Thread(runnable,"Thread_"+i);
            thread.start();
        }
        countDownLatch.await();
        Long end = System.currentTimeMillis();
        System.out.println("经历："+(end-start));
    }



}
