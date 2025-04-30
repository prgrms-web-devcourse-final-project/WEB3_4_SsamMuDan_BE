package ssammudan.cotree.domain.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


/**
 * PackageName : ssammudan.cotree.domain.common.controller
 * FileName    : HealthCheckController
 * Author      : Baekgwa
 * Date        : 2025-04-25
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025-04-25     Baekgwa               Initial creation
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
@Tag(name = "Health Check Controller", description = "Health Check API")
public class HealthCheckController {

	@GetMapping
	public ResponseEntity<String> simplePing() {
		return ResponseEntity.ok("OK");
	}
}
