package com.excentria_it.wamya.application.service;

import com.excentria_it.wamya.application.port.in.CreateDiscussionUseCase.CreateDiscussionCommand;
import com.excentria_it.wamya.application.port.in.CreateDiscussionUseCase.CreateDiscussionCommand.CreateDiscussionCommandBuilder;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByClientIdAndTransporterIdCommand;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByClientIdAndTransporterIdCommand.FindDiscussionByClientIdAndTransporterIdCommandBuilder;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByIdCommand;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByIdCommand.FindDiscussionByIdCommandBuilder;
import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase.LoadDiscussionsCommand.LoadDiscussionsCommandBuilder;
import com.excentria_it.wamya.application.port.out.CreateDiscussionPort;
import com.excentria_it.wamya.application.port.out.LoadDiscussionsPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.exception.DiscussionNotFoundException;
import com.excentria_it.wamya.common.exception.OperationDeniedException;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.*;
import com.excentria_it.wamya.test.data.common.OAuthId;
import com.excentria_it.wamya.test.data.common.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.util.Optional;

import static com.excentria_it.wamya.test.data.common.DiscussionTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class DiscussionsServiceTests {

    @Mock
    private LoadDiscussionsPort loadDiscussionsPort;
    @Mock
    private LoadUserAccountPort loadUserAccountPort;
    @Mock
    private CreateDiscussionPort createDiscussionPort;
    @Spy
    private DateTimeHelper dateTimeHelper;

    @InjectMocks
    private DiscussionsService discussionsService;

    @Test
    void testLoadDiscussions() {
        // given
        UserAccount userAccount = defaultUserAccountBuilder().build();
        given(loadUserAccountPort.loadUserAccountBySubject(any(String.class))).willReturn(Optional.of(userAccount));

        ZoneId userZoneId = ZoneId.of("Africa/Tunis");
        doReturn(userZoneId).when(dateTimeHelper).findUserZoneId(any(String.class));

        LoadDiscussionsOutputResult expectedResult = defaultLoadDiscussionsOutputResult();
        given(loadDiscussionsPort.loadDiscussions(any(Long.class), any(Boolean.class), any(Integer.class),
                any(Integer.class), any(FilterCriterion.class), any(SortCriterion.class))).willReturn(expectedResult);

        LoadDiscussionsCommandBuilder commandBuilder = defaultLoadDiscussionsCommandBuilder();

        // when

        LoadDiscussionsResult loadDiscussionsResult = discussionsService.loadDiscussions(commandBuilder.build());

        // then

        assertEquals(expectedResult.getPageSize(), loadDiscussionsResult.getPageSize());
        assertEquals(expectedResult.getPageNumber(), loadDiscussionsResult.getPageNumber());
        assertEquals(expectedResult.getTotalElements(), loadDiscussionsResult.getTotalElements());
        assertEquals(expectedResult.getTotalPages(), loadDiscussionsResult.getTotalPages());
        assertEquals(expectedResult.getContent().size(), loadDiscussionsResult.getContent().size());

        for (int i = 0; i < loadDiscussionsResult.getContent().size(); i++) {
            assertEquals(expectedResult.getContent().get(i).getId(), loadDiscussionsResult.getContent().get(i).getId());
            assertEquals(expectedResult.getContent().get(i).getActive(),
                    loadDiscussionsResult.getContent().get(i).getActive());

            assertEquals(expectedResult.getContent().get(i).getClient().getId(),
                    loadDiscussionsResult.getContent().get(i).getClient().getId());
            assertEquals(expectedResult.getContent().get(i).getClient().getName(),
                    loadDiscussionsResult.getContent().get(i).getClient().getName());
            assertEquals(expectedResult.getContent().get(i).getClient().getMobileNumber(),
                    loadDiscussionsResult.getContent().get(i).getClient().getMobileNumber());
            assertEquals(expectedResult.getContent().get(i).getClient().getPhotoUrl(),
                    loadDiscussionsResult.getContent().get(i).getClient().getPhotoUrl());

            assertEquals(expectedResult.getContent().get(i).getTransporter().getId(),
                    loadDiscussionsResult.getContent().get(i).getTransporter().getId());
            assertEquals(expectedResult.getContent().get(i).getTransporter().getName(),
                    loadDiscussionsResult.getContent().get(i).getTransporter().getName());
            assertEquals(expectedResult.getContent().get(i).getTransporter().getMobileNumber(),
                    loadDiscussionsResult.getContent().get(i).getTransporter().getMobileNumber());
            assertEquals(expectedResult.getContent().get(i).getTransporter().getPhotoUrl(),
                    loadDiscussionsResult.getContent().get(i).getTransporter().getPhotoUrl());

            assertEquals(expectedResult.getContent().get(i).getDateTime(),
                    loadDiscussionsResult.getContent().get(i).getDateTime().atZone(userZoneId).toInstant());

            assertEquals(expectedResult.getContent().get(i).getLatestMessage().getId(),
                    loadDiscussionsResult.getContent().get(i).getLatestMessage().getId());
            assertEquals(expectedResult.getContent().get(i).getLatestMessage().getAuthorId(),
                    loadDiscussionsResult.getContent().get(i).getLatestMessage().getAuthorId());

            assertEquals(expectedResult.getContent().get(i).getLatestMessage().getContent(),
                    loadDiscussionsResult.getContent().get(i).getLatestMessage().getContent());

            assertEquals(expectedResult.getContent().get(i).getLatestMessage().getRead(),
                    loadDiscussionsResult.getContent().get(i).getLatestMessage().getRead());

            assertEquals(expectedResult.getContent().get(i).getLatestMessage().getDateTime(), loadDiscussionsResult
                    .getContent().get(i).getLatestMessage().getDateTime().atZone(userZoneId).toInstant());
        }

    }

    @Test
    void testLoadDiscussionsWithNullFilterCriterion() {
        // given
        UserAccount userAccount = defaultUserAccountBuilder().build();
        given(loadUserAccountPort.loadUserAccountBySubject(any(String.class))).willReturn(Optional.of(userAccount));

        ZoneId userZoneId = ZoneId.of("Africa/Tunis");
        doReturn(userZoneId).when(dateTimeHelper).findUserZoneId(any(String.class));

        LoadDiscussionsOutputResult expectedResult = defaultLoadDiscussionsOutputResult();
        given(loadDiscussionsPort.loadDiscussions(any(Long.class), any(Boolean.class), any(Integer.class),
                any(Integer.class), any(SortCriterion.class))).willReturn(expectedResult);

        // when

        LoadDiscussionsCommandBuilder commandBuilder = defaultLoadDiscussionsCommandBuilder();
        LoadDiscussionsResult loadDiscussionsResult = discussionsService
                .loadDiscussions(commandBuilder.filteringCriterion(null).build());

        // then

        assertEquals(expectedResult.getPageSize(), loadDiscussionsResult.getPageSize());
        assertEquals(expectedResult.getPageNumber(), loadDiscussionsResult.getPageNumber());
        assertEquals(expectedResult.getTotalElements(), loadDiscussionsResult.getTotalElements());
        assertEquals(expectedResult.getTotalPages(), loadDiscussionsResult.getTotalPages());
        assertEquals(expectedResult.getContent().size(), loadDiscussionsResult.getContent().size());

        for (int i = 0; i < loadDiscussionsResult.getContent().size(); i++) {

            assertEquals(expectedResult.getContent().get(i).getId(), loadDiscussionsResult.getContent().get(i).getId());
            assertEquals(expectedResult.getContent().get(i).getActive(),
                    loadDiscussionsResult.getContent().get(i).getActive());

            assertEquals(expectedResult.getContent().get(i).getClient().getId(),
                    loadDiscussionsResult.getContent().get(i).getClient().getId());
            assertEquals(expectedResult.getContent().get(i).getClient().getName(),
                    loadDiscussionsResult.getContent().get(i).getClient().getName());
            assertEquals(expectedResult.getContent().get(i).getClient().getMobileNumber(),
                    loadDiscussionsResult.getContent().get(i).getClient().getMobileNumber());
            assertEquals(expectedResult.getContent().get(i).getClient().getPhotoUrl(),
                    loadDiscussionsResult.getContent().get(i).getClient().getPhotoUrl());

            assertEquals(expectedResult.getContent().get(i).getTransporter().getId(),
                    loadDiscussionsResult.getContent().get(i).getTransporter().getId());
            assertEquals(expectedResult.getContent().get(i).getTransporter().getName(),
                    loadDiscussionsResult.getContent().get(i).getTransporter().getName());
            assertEquals(expectedResult.getContent().get(i).getTransporter().getMobileNumber(),
                    loadDiscussionsResult.getContent().get(i).getTransporter().getMobileNumber());
            assertEquals(expectedResult.getContent().get(i).getTransporter().getPhotoUrl(),
                    loadDiscussionsResult.getContent().get(i).getTransporter().getPhotoUrl());

            assertEquals(expectedResult.getContent().get(i).getDateTime(),
                    loadDiscussionsResult.getContent().get(i).getDateTime().atZone(userZoneId).toInstant());

            assertEquals(expectedResult.getContent().get(i).getLatestMessage().getId(),
                    loadDiscussionsResult.getContent().get(i).getLatestMessage().getId());
            assertEquals(expectedResult.getContent().get(i).getLatestMessage().getAuthorId(),
                    loadDiscussionsResult.getContent().get(i).getLatestMessage().getAuthorId());

            assertEquals(expectedResult.getContent().get(i).getLatestMessage().getContent(),
                    loadDiscussionsResult.getContent().get(i).getLatestMessage().getContent());

            assertEquals(expectedResult.getContent().get(i).getLatestMessage().getRead(),
                    loadDiscussionsResult.getContent().get(i).getLatestMessage().getRead());

            assertEquals(expectedResult.getContent().get(i).getLatestMessage().getDateTime(), loadDiscussionsResult
                    .getContent().get(i).getLatestMessage().getDateTime().atZone(userZoneId).toInstant());

        }

    }

    @Test
    void givenBadUsername_WhenLoadDiscussions_ThenThrowUserAccountNotFoundException() {
        // given
        given(loadUserAccountPort.loadUserAccountBySubject(any(String.class))).willReturn(Optional.empty());

        LoadDiscussionsCommandBuilder commandBuilder = defaultLoadDiscussionsCommandBuilder();

        // when
        // then

        assertThrows(UserAccountNotFoundException.class,
                () -> discussionsService.loadDiscussions(commandBuilder.build()));

    }

    @Test
    void testFindDiscussionByClientIdAndTransporterId() {
        // given
        FindDiscussionByClientIdAndTransporterIdCommandBuilder commandBuilder = defaultFindDiscussionByClientIdAndTransporterIdCommandBuilder();
        FindDiscussionByClientIdAndTransporterIdCommand command = commandBuilder.build();
        UserAccount clientUserAccount = defaultClientUserAccountBuilder().build();

        given(loadUserAccountPort.loadUserAccountBySubject(any(String.class)))
                .willReturn(Optional.of(clientUserAccount));

        LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
        given(loadDiscussionsPort.loadDiscussionByClientIdAndTransporterId(any(String.class), any(String.class)))
                .willReturn(Optional.of(loadDiscussionsOutput));
        ZoneId userZoneId = ZoneId.of("Africa/Tunis");
        doReturn(userZoneId).when(dateTimeHelper).findUserZoneId(any(String.class));

        // when
        LoadDiscussionsDto loadDiscussionsDto = discussionsService.findDiscussionByClientIdAndTransporterId(command);
        // then

        then(loadUserAccountPort).should(times(1)).loadUserAccountBySubject(command.getSubject());
        then(loadDiscussionsPort).should(times(1)).loadDiscussionByClientIdAndTransporterId(command.getClientId(),
                command.getTransporterId());

        assertEquals(loadDiscussionsOutput.getId(), loadDiscussionsDto.getId());
        assertEquals(loadDiscussionsOutput.getActive(), loadDiscussionsDto.getActive());
        assertEquals(loadDiscussionsOutput.getDateTime(),
                loadDiscussionsDto.getDateTime().atZone(userZoneId).toInstant());

        assertEquals(loadDiscussionsOutput.getLatestMessage().getId(), loadDiscussionsDto.getLatestMessage().getId());
        assertEquals(loadDiscussionsOutput.getLatestMessage().getAuthorId(),
                loadDiscussionsDto.getLatestMessage().getAuthorId());
        assertEquals(loadDiscussionsOutput.getLatestMessage().getContent(),
                loadDiscussionsDto.getLatestMessage().getContent());
        assertEquals(loadDiscussionsOutput.getLatestMessage().getRead(),
                loadDiscussionsDto.getLatestMessage().getRead());
        assertEquals(loadDiscussionsOutput.getLatestMessage().getDateTime(),
                loadDiscussionsDto.getLatestMessage().getDateTime().atZone(userZoneId).toInstant());

        assertEquals(loadDiscussionsOutput.getClient().getId(), loadDiscussionsDto.getClient().getId());
        assertEquals(loadDiscussionsOutput.getClient().getMobileNumber(),
                loadDiscussionsDto.getClient().getMobileNumber());
        assertEquals(loadDiscussionsOutput.getClient().getName(), loadDiscussionsDto.getClient().getName());
        assertEquals(loadDiscussionsOutput.getClient().getPhotoUrl(), loadDiscussionsDto.getClient().getPhotoUrl());

        assertEquals(loadDiscussionsOutput.getTransporter().getId(), loadDiscussionsDto.getTransporter().getId());
        assertEquals(loadDiscussionsOutput.getTransporter().getMobileNumber(),
                loadDiscussionsDto.getTransporter().getMobileNumber());
        assertEquals(loadDiscussionsOutput.getTransporter().getName(), loadDiscussionsDto.getTransporter().getName());
        assertEquals(loadDiscussionsOutput.getTransporter().getPhotoUrl(),
                loadDiscussionsDto.getTransporter().getPhotoUrl());

    }

    @Test
    void givenCommandClientIdAndTransporterIdAreDifferentFromAuthenticatedUserId_WhenFindDiscussion_ThenThrowDiscussionNotFoundException() {
        // given
        FindDiscussionByClientIdAndTransporterIdCommandBuilder commandBuilder = defaultFindDiscussionByClientIdAndTransporterIdCommandBuilder();
        FindDiscussionByClientIdAndTransporterIdCommand command = commandBuilder.clientId(OAuthId.CLIENT1_UUID).transporterId(OAuthId.TRANSPORTER1_UUID).subject("some-other-username")
                .build();
        UserAccount userAccount = defaultUserAccountBuilder().build();

        given(loadUserAccountPort.loadUserAccountBySubject(any(String.class)))
                .willReturn(Optional.of(userAccount));

        // when
        // then
        assertThrows(DiscussionNotFoundException.class,
                () -> discussionsService.findDiscussionByClientIdAndTransporterId(command));

    }

    @Test
    void givenInexistentDiscussion_WhenFindDiscussion_ThenThrowDiscussionNotFoundException() {
        // given
        FindDiscussionByClientIdAndTransporterIdCommandBuilder commandBuilder = defaultFindDiscussionByClientIdAndTransporterIdCommandBuilder();
        FindDiscussionByClientIdAndTransporterIdCommand command = commandBuilder.build();
        UserAccount clientUserAccount = defaultClientUserAccountBuilder().build();

        given(loadUserAccountPort.loadUserAccountBySubject(any(String.class)))
                .willReturn(Optional.of(clientUserAccount));
        given(loadDiscussionsPort.loadDiscussionByClientIdAndTransporterId(any(String.class), any(String.class)))
                .willReturn(Optional.empty());
        // when
        // then
        assertThrows(DiscussionNotFoundException.class,
                () -> discussionsService.findDiscussionByClientIdAndTransporterId(command));

    }

    @Test
    void testFindDiscussionById() {
        // given
        FindDiscussionByIdCommandBuilder commandBuilder = defaultFindDiscussionByIdCommandBuilder();
        FindDiscussionByIdCommand command = commandBuilder.build();


        LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
        given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));
        ZoneId userZoneId = ZoneId.of("Africa/Tunis");
        doReturn(userZoneId).when(dateTimeHelper).findUserZoneId(any(String.class));

        // when
        LoadDiscussionsDto loadDiscussionsDto = discussionsService.findDiscussionById(command);
        // then

        then(loadDiscussionsPort).should(times(1)).loadDiscussionById(command.getDiscussionId());

        assertEquals(loadDiscussionsOutput.getId(), loadDiscussionsDto.getId());
        assertEquals(loadDiscussionsOutput.getActive(), loadDiscussionsDto.getActive());
        assertEquals(loadDiscussionsOutput.getDateTime(),
                loadDiscussionsDto.getDateTime().atZone(userZoneId).toInstant());

        assertEquals(loadDiscussionsOutput.getLatestMessage().getId(), loadDiscussionsDto.getLatestMessage().getId());
        assertEquals(loadDiscussionsOutput.getLatestMessage().getAuthorId(),
                loadDiscussionsDto.getLatestMessage().getAuthorId());
        assertEquals(loadDiscussionsOutput.getLatestMessage().getContent(),
                loadDiscussionsDto.getLatestMessage().getContent());
        assertEquals(loadDiscussionsOutput.getLatestMessage().getRead(),
                loadDiscussionsDto.getLatestMessage().getRead());
        assertEquals(loadDiscussionsOutput.getLatestMessage().getDateTime(),
                loadDiscussionsDto.getLatestMessage().getDateTime().atZone(userZoneId).toInstant());

        assertEquals(loadDiscussionsOutput.getClient().getId(), loadDiscussionsDto.getClient().getId());
        assertEquals(loadDiscussionsOutput.getClient().getMobileNumber(),
                loadDiscussionsDto.getClient().getMobileNumber());
        assertEquals(loadDiscussionsOutput.getClient().getName(), loadDiscussionsDto.getClient().getName());
        assertEquals(loadDiscussionsOutput.getClient().getPhotoUrl(), loadDiscussionsDto.getClient().getPhotoUrl());

        assertEquals(loadDiscussionsOutput.getTransporter().getId(), loadDiscussionsDto.getTransporter().getId());
        assertEquals(loadDiscussionsOutput.getTransporter().getMobileNumber(),
                loadDiscussionsDto.getTransporter().getMobileNumber());
        assertEquals(loadDiscussionsOutput.getTransporter().getName(), loadDiscussionsDto.getTransporter().getName());
        assertEquals(loadDiscussionsOutput.getTransporter().getPhotoUrl(),
                loadDiscussionsDto.getTransporter().getPhotoUrl());

    }

    @Test
    void givenDiscussionClientEmailAndTransporterEmailAreDifferentFromAuthenticatedUserEmail_WhenFindDiscussionById_ThenThrowDiscussionNotFoundException() {
        // given
        FindDiscussionByIdCommandBuilder commandBuilder = defaultFindDiscussionByIdCommandBuilder();
        FindDiscussionByIdCommand command = commandBuilder.discussionId(600L).subject("some_username@gmail.com")
                .build();
        LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
        given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

        // when

        // then
        assertThrows(DiscussionNotFoundException.class, () -> discussionsService.findDiscussionById(command));

    }

    @Test
    void givenInexistentDiscussion_WhenFindDiscussionById_ThenThrowDiscussionNotFoundException() {
        // given
        FindDiscussionByIdCommandBuilder commandBuilder = defaultFindDiscussionByIdCommandBuilder();
        FindDiscussionByIdCommand command = commandBuilder.build();

        given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.empty());
        // when
        // then
        assertThrows(DiscussionNotFoundException.class, () -> discussionsService.findDiscussionById(command));

    }

    @Test
    void testCreateDiscussion() {
        CreateDiscussionCommandBuilder commandBuilder = defaultCreateDiscussionCommandBuilder();
        CreateDiscussionCommand command = commandBuilder.build();
        UserAccount clientUserAccount = defaultClientUserAccountBuilder().build();

        given(loadUserAccountPort.loadUserAccountBySubject(any(String.class)))
                .willReturn(Optional.of(clientUserAccount));

        given(loadUserAccountPort.existsByOauthId(any(String.class))).willReturn(true);

        LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
        given(createDiscussionPort.createDiscussion(any(String.class), any(String.class)))
                .willReturn(loadDiscussionsOutput);

        ZoneId userZoneId = ZoneId.of("Africa/Tunis");
        doReturn(userZoneId).when(dateTimeHelper).findUserZoneId(any(String.class));

        // when
        LoadDiscussionsDto loadDiscussionsDto = discussionsService.createDiscussion(command,
                TestConstants.DEFAULT_EMAIL);
        // then

        then(loadUserAccountPort).should(times(1)).loadUserAccountBySubject(TestConstants.DEFAULT_EMAIL);
        then(loadUserAccountPort).should(times(1)).existsByOauthId(command.getTransporterId());
        then(createDiscussionPort).should(times(1)).createDiscussion(command.getClientId(), command.getTransporterId());

        assertEquals(loadDiscussionsOutput.getId(), loadDiscussionsDto.getId());
        assertEquals(loadDiscussionsOutput.getActive(), loadDiscussionsDto.getActive());
        assertEquals(loadDiscussionsOutput.getDateTime(),
                loadDiscussionsDto.getDateTime().atZone(userZoneId).toInstant());

        assertEquals(loadDiscussionsOutput.getLatestMessage().getId(), loadDiscussionsDto.getLatestMessage().getId());
        assertEquals(loadDiscussionsOutput.getLatestMessage().getAuthorId(),
                loadDiscussionsDto.getLatestMessage().getAuthorId());
        assertEquals(loadDiscussionsOutput.getLatestMessage().getContent(),
                loadDiscussionsDto.getLatestMessage().getContent());
        assertEquals(loadDiscussionsOutput.getLatestMessage().getRead(),
                loadDiscussionsDto.getLatestMessage().getRead());
        assertEquals(loadDiscussionsOutput.getLatestMessage().getDateTime(),
                loadDiscussionsDto.getLatestMessage().getDateTime().atZone(userZoneId).toInstant());

        assertEquals(loadDiscussionsOutput.getClient().getId(), loadDiscussionsDto.getClient().getId());
        assertEquals(loadDiscussionsOutput.getClient().getMobileNumber(),
                loadDiscussionsDto.getClient().getMobileNumber());
        assertEquals(loadDiscussionsOutput.getClient().getName(), loadDiscussionsDto.getClient().getName());
        assertEquals(loadDiscussionsOutput.getClient().getPhotoUrl(), loadDiscussionsDto.getClient().getPhotoUrl());

        assertEquals(loadDiscussionsOutput.getTransporter().getId(), loadDiscussionsDto.getTransporter().getId());
        assertEquals(loadDiscussionsOutput.getTransporter().getMobileNumber(),
                loadDiscussionsDto.getTransporter().getMobileNumber());
        assertEquals(loadDiscussionsOutput.getTransporter().getName(), loadDiscussionsDto.getTransporter().getName());
        assertEquals(loadDiscussionsOutput.getTransporter().getPhotoUrl(),
                loadDiscussionsDto.getTransporter().getPhotoUrl());
    }

    @Test
    void givenTransporterAuthenticatedOauthIdIsDifferentFromTransporterId_WhenCreateDiscussion_ThenThrowOperationDeniedException() {
        UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder().build();

        CreateDiscussionCommandBuilder commandBuilder = defaultCreateDiscussionCommandBuilder();
        CreateDiscussionCommand command = commandBuilder.transporterId("some-other-uuid").build();


        given(loadUserAccountPort.loadUserAccountBySubject(eq(TestConstants.DEFAULT_EMAIL)))
                .willReturn(Optional.of(transporterUserAccount));

        assertThrows(OperationDeniedException.class,
                () -> discussionsService.createDiscussion(command, TestConstants.DEFAULT_EMAIL));
    }

    @Test
    void givenClientAuthenticatedOauthIdIsDifferentFromClientId_WhenCreateDiscussion_ThenThrowOperationDeniedException() {
        UserAccount clientUserAccount = defaultClientUserAccountBuilder().build();

        CreateDiscussionCommandBuilder commandBuilder = defaultCreateDiscussionCommandBuilder();
        CreateDiscussionCommand command = commandBuilder.clientId(clientUserAccount.getOauthId()).build();


        given(loadUserAccountPort.loadUserAccountBySubject(eq(TestConstants.DEFAULT_EMAIL)))
                .willReturn(Optional.of(clientUserAccount));

        assertThrows(OperationDeniedException.class,
                () -> discussionsService.createDiscussion(command, TestConstants.DEFAULT_EMAIL));
    }

    @Test
    void givenClientAuthenticatedUserAndInexistentTransporter_WhenCreateDiscussion_ThenThrowOperationDeniedException() {
        CreateDiscussionCommandBuilder commandBuilder = defaultCreateDiscussionCommandBuilder();
        CreateDiscussionCommand command = commandBuilder.build();

        UserAccount clientUserAccount = defaultClientUserAccountBuilder().build();

        given(loadUserAccountPort.loadUserAccountBySubject(any(String.class)))
                .willReturn(Optional.of(clientUserAccount));
        given(loadUserAccountPort.existsByOauthId(any(String.class))).willReturn(false);

        assertThrows(OperationDeniedException.class,
                () -> discussionsService.createDiscussion(command, TestConstants.DEFAULT_EMAIL));
    }

    @Test
    void givenTransporterAuthenticatedUserAndInexistentClient_WhenCreateDiscussion_ThenThrowOperationDeniedException() {
        CreateDiscussionCommandBuilder commandBuilder = defaultCreateDiscussionCommandBuilder();
        CreateDiscussionCommand command = commandBuilder.build();

        UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder().build();

        given(loadUserAccountPort.loadUserAccountBySubject(any(String.class)))
                .willReturn(Optional.of(transporterUserAccount));
        given(loadUserAccountPort.existsByOauthId(any(String.class))).willReturn(false);

        assertThrows(OperationDeniedException.class,
                () -> discussionsService.createDiscussion(command, TestConstants.DEFAULT_EMAIL));
    }
}
