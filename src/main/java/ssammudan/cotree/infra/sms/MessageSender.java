package ssammudan.cotree.infra.sms;

import net.nurigo.sdk.message.model.Message;

/**
 * PackageName : ssammudan.cotree.infra.sms
 * FileName    : MessageSender
 * Author      : kwak
 * Date        : 2025. 4. 13.
 * Description : 
 * =====================================================================================================================
 * DATE          AUTHOR               NOTE
 * ---------------------------------------------------------------------------------------------------------------------
 * 2025. 4. 13.     kwak               Initial creation
 */
public interface MessageSender {

	void sendOne(Message message);
}
