package pl.coderslab;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class CrmApplication implements AsyncConfigurer, SchedulingConfigurer{

	
	public static void main(String[] args) {
		SpringApplication.run(CrmApplication.class, args);
	}
	

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
	    taskExecutor.setMaxPoolSize(10);
	    taskExecutor.setThreadNamePrefix("CustomExecutor-");
	    taskExecutor.initialize();
	    return taskExecutor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SimpleAsyncUncaughtExceptionHandler();
	}


	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler thPoolTaskScheduler = new ThreadPoolTaskScheduler();
	    thPoolTaskScheduler.setPoolSize(10);
	    thPoolTaskScheduler.setThreadNamePrefix("my-scheduled-task-pool-");
	    thPoolTaskScheduler.initialize();
	    taskRegistrar.setTaskScheduler(thPoolTaskScheduler);
	}
}
