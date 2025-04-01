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
 */
@Converter(autoApply = true)
public class DurationToLongConverter implements AttributeConverter<Duration, Long> {

	@Override
	public Long convertToDatabaseColumn(Duration duration) {
		return (duration != null) ? duration.getSeconds() : null;
	}

	@Override
	public Duration convertToEntityAttribute(Long aLong) {
		return (aLong != null) ? Duration.ofSeconds(aLong) : null;
	}

}
