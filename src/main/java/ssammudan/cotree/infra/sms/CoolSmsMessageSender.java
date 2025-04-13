package ssammudan.cotree.infra.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

import ssammudan.cotree.global.error.GlobalException;
import ssammudan.cotree.global.response.ErrorCode;

/**
 * PackageName : ssammudan.cotree.infra.sms
 * FileName    : CoolSmsMessageSender
 * Author      : kwak
 * Date        : 2025. 4. 13.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 13.     kwak               Initial creation
 */
@Component
public class CoolSmsMessageSender implements MessageSender {

	private final DefaultMessageService messageService;
	private static final String coolSmsUrl = "https://api.coolsms.co.kr";

	public CoolSmsMessageSender(
		@Value("${COOL_SMS_API_KEY}") String apiKey,
		@Value("${COOL_SMS_SECRET_KEY}") String apiSecret
	) {
		this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, coolSmsUrl);
	}

	@Override
	public void sendOne(Message message) {
		try {
			messageService.sendOne(new SingleMessageSendingRequest(message));
		} catch (Exception e) {
			throw new GlobalException(ErrorCode.SMS_SEND_FAILED);
		}
	}
}
