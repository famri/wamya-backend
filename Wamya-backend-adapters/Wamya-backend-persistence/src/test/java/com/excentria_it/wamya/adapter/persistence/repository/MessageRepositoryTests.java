package com.excentria_it.wamya.adapter.persistence.repository;

import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GenderJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.MessageJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;
import com.excentria_it.wamya.test.data.common.GenderJpaTestData;
import com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class MessageRepositoryTests {
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private DiscussionRepository discussionRepository;

	@Autowired
	private InternationalCallingCodeRepository iccRepository;

	@Autowired
	private GenderRepository genderRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@BeforeEach
	void cleanDatabase() {
		messageRepository.deleteAll();
		discussionRepository.deleteAll();

		userAccountRepository.deleteAll();
		iccRepository.deleteAll();
		genderRepository.deleteAll();
		documentRepository.deleteAll();
	}

	@Test
	void testCountClientMessages() {
		List<UserAccountJpaEntity> userAccounts = givenUserAccounts();

		List<DiscussionJpaEntity> discussions = givenDiscussion(userAccounts);

		List<MessageJpaEntity> messages = givenMessages(userAccounts, discussions);

		Long unreadMesages = messageRepository.countClientMessages(userAccounts.get(0).getEmail(), false);

		List<MessageJpaEntity> clientUnreadMessages = messages.stream()
				.filter(m -> !m.getAuthor().getId().equals(userAccounts.get(0).getId()) && !m.getRead()
						&& m.getDiscussion().getClient().getEmail().equals(userAccounts.get(0).getEmail()))
				.collect(Collectors.toList());

		assertEquals(clientUnreadMessages.size(), unreadMesages);
	}

	@Test
	void testCountTransporter1Messages() {

		List<UserAccountJpaEntity> userAccounts = givenUserAccounts();

		List<DiscussionJpaEntity> discussions = givenDiscussion(userAccounts);

		List<MessageJpaEntity> messages = givenMessages(userAccounts, discussions);

		Long unreadMesages = messageRepository.countTransporterMessages(userAccounts.get(1).getEmail(), false);

		List<MessageJpaEntity> transporter1UnreadMessages = messages.stream()
				.filter(m -> !m.getAuthor().getId().equals(userAccounts.get(1).getId()) && !m.getRead()
						&& m.getDiscussion().getTransporter().getEmail().equals(userAccounts.get(1).getEmail()))
				.collect(Collectors.toList());

		assertEquals(transporter1UnreadMessages.size(), unreadMesages);
	}

	private List<DiscussionJpaEntity> givenDiscussion(List<UserAccountJpaEntity> userAccounts) {
		DiscussionJpaEntity discussion1 = DiscussionJpaEntity.builder().active(true)
				.client((ClientJpaEntity) userAccounts.get(0)).transporter((TransporterJpaEntity) userAccounts.get(1))
				.dateTime(Instant.now()).build();

		DiscussionJpaEntity discussion2 = DiscussionJpaEntity.builder().active(true)
				.client((ClientJpaEntity) userAccounts.get(0)).transporter((TransporterJpaEntity) userAccounts.get(2))
				.dateTime(Instant.now()).build();

		discussion1 = discussionRepository.save(discussion1);

		discussion2 = discussionRepository.save(discussion2);

		return List.of(discussion1, discussion2);
	}

	private List<MessageJpaEntity> givenMessages(List<UserAccountJpaEntity> userAccounts,
			List<DiscussionJpaEntity> discussions) {

		MessageJpaEntity message1 = MessageJpaEntity.builder().author(userAccounts.get(0)).read(true)
				.content("Client message 1 is read").dateTime(Instant.now()).discussion(discussions.get(0)).build();
		message1 = messageRepository.save(message1);

		MessageJpaEntity message2 = MessageJpaEntity.builder().author(userAccounts.get(0)).read(false)
				.content("Client message 2 is unread").dateTime(Instant.now()).discussion(discussions.get(0)).build();

		message2 = messageRepository.save(message2);

		MessageJpaEntity message3 = MessageJpaEntity.builder().author(userAccounts.get(0)).read(true)
				.content("Client message 3 is read").dateTime(Instant.now()).discussion(discussions.get(1)).build();
		message3 = messageRepository.save(message3);

		MessageJpaEntity message4 = MessageJpaEntity.builder().author(userAccounts.get(0)).read(false)
				.content("Client message 3 is unread").dateTime(Instant.now()).discussion(discussions.get(1)).build();
		message4 = messageRepository.save(message4);

		MessageJpaEntity message5 = MessageJpaEntity.builder().author(userAccounts.get(1)).read(true)
				.content("Transporter 1 message 1 is read").dateTime(Instant.now()).discussion(discussions.get(0))
				.build();
		message5 = messageRepository.save(message5);

		MessageJpaEntity message6 = MessageJpaEntity.builder().author(userAccounts.get(1)).read(false)
				.content("Transporter 1 message 2 is unread").dateTime(Instant.now()).discussion(discussions.get(0))
				.build();
		message6 = messageRepository.save(message6);

		MessageJpaEntity message7 = MessageJpaEntity.builder().author(userAccounts.get(1)).read(true)
				.content("Transporter 1 message 3 is read").dateTime(Instant.now()).discussion(discussions.get(0))
				.build();
		message7 = messageRepository.save(message7);

		MessageJpaEntity message8 = MessageJpaEntity.builder().author(userAccounts.get(1)).read(false)
				.content("Transporter 1 message 4 is unread").dateTime(Instant.now()).discussion(discussions.get(0))
				.build();
		message8 = messageRepository.save(message8);

		MessageJpaEntity message9 = MessageJpaEntity.builder().author(userAccounts.get(2)).read(true)
				.content("Transporter 2 message 1 is read").dateTime(Instant.now()).discussion(discussions.get(1))
				.build();
		message9 = messageRepository.save(message9);

		MessageJpaEntity message10 = MessageJpaEntity.builder().author(userAccounts.get(2)).read(false)
				.content("Transporter 2 message 2 is unread").dateTime(Instant.now()).discussion(discussions.get(1))
				.build();
		message10 = messageRepository.save(message10);

		MessageJpaEntity message11 = MessageJpaEntity.builder().author(userAccounts.get(2)).read(true)
				.content("Transporter 2 message 3 is read").dateTime(Instant.now()).discussion(discussions.get(1))
				.build();
		message11 = messageRepository.save(message11);

		MessageJpaEntity message12 = MessageJpaEntity.builder().author(userAccounts.get(2)).read(false)
				.content("Transporter 2 message 4 is unread").dateTime(Instant.now()).discussion(discussions.get(1))
				.build();
		message12 = messageRepository.save(message12);

		List<MessageJpaEntity> messages = List.of(message1, message2, message3, message4, message5, message6, message7,
				message8, message9, message10, message11, message12);

		return messages;
	}

	private List<UserAccountJpaEntity> givenUserAccounts() {

		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultNewInternationalCallingCodeJpaEntity();

		iccEntity = iccRepository.save(iccEntity);

		GenderJpaEntity genderEntity = GenderJpaTestData.manGenderJpaEntity();
		genderEntity = genderRepository.save(genderEntity);

		DocumentJpaEntity defaultManProfileImage = DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity();
		defaultManProfileImage = documentRepository.save(defaultManProfileImage);

		UserAccountJpaEntity clientAccountEntity = defaultNewClientJpaEntity();
		clientAccountEntity.setEmail("client1@gmail.com");
		clientAccountEntity.setIcc(iccEntity);
		clientAccountEntity.setGender(genderEntity);
		clientAccountEntity.setProfileImage(defaultManProfileImage);
		clientAccountEntity = userAccountRepository.save(clientAccountEntity);

		UserAccountJpaEntity transporterAccountEntity1 = defaultNewTransporterJpaEntity();
		transporterAccountEntity1.setEmail("transporter1@gmail.com");
		transporterAccountEntity1.setIcc(iccEntity);
		transporterAccountEntity1.setGender(genderEntity);
		transporterAccountEntity1.setProfileImage(defaultManProfileImage);
		transporterAccountEntity1 = userAccountRepository.save(transporterAccountEntity1);

		UserAccountJpaEntity transporterAccountEntity2 = defaultNewTransporterJpaEntity();
		transporterAccountEntity2.setEmail("transporter2@gmail.com");
		transporterAccountEntity2.setIcc(iccEntity);
		transporterAccountEntity2.setGender(genderEntity);
		transporterAccountEntity2.setProfileImage(defaultManProfileImage);
		transporterAccountEntity2 = userAccountRepository.save(transporterAccountEntity2);

		return List.of(clientAccountEntity, transporterAccountEntity1, transporterAccountEntity2);
	}

}
