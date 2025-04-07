package ssammudan.cotree.global.converter;

import java.time.Duration;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * PackageName : ssammudan.cotree.global.converter
 * FileName    : DurationToLongConverter
 * Author      : loadingKKamo21
 * Date        : 25. 3. 30.
 * Description : 엔티티 Duration <-> DB Long 변환 컨버터
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 25. 3. 30.    loadingKKamo21       Initial creation
 * 25. 4. 7.     Baekgwa       		  사용하지 않아, deprecated 처리
 */
@Converter(autoApply = true)
@Deprecated(forRemoval = true)
public class DurationToLongConverter implements AttributeConverter<Duration, Long> {

	@Override
	@Deprecated(forRemoval = true)
	public Long convertToDatabaseColumn(Duration duration) {
		return (duration != null) ? duration.getSeconds() : null;
	}

	@Override
	@Deprecated(forRemoval = true)
	public Duration convertToEntityAttribute(Long aLong) {
		return (aLong != null) ? Duration.ofSeconds(aLong) : null;
	}

}
