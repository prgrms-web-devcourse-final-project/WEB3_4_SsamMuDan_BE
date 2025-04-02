package ssammudan.cotree.global.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * PackageName : ssammudan.cotree.global.config
 * FileName    : AsyncConfig
 * Author      : kwak
 * Date        : 2025. 4. 1.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 1.     kwak               Initial creation
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);               // 기본 스레드 수
		executor.setMaxPoolSize(10);               // 최대 스레드 수
		executor.setQueueCapacity(50);             // 대기 큐 크기
		executor.setThreadNamePrefix("Async-");    // 스레드 이름 접두사
		executor.setWaitForTasksToCompleteOnShutdown(true); // 종료 시 작업 완료 대기
		executor.setAwaitTerminationSeconds(60);   // 최대 종료 대기 시간(초)
		executor.initialize();
		return executor;
	}
}
