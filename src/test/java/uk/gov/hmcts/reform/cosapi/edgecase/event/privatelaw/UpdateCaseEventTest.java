package uk.gov.hmcts.reform.cosapi.edgecase.event.privatelaw;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.reform.cosapi.edgecase.model.CaseData;
import uk.gov.hmcts.reform.cosapi.edgecase.model.State;
import uk.gov.hmcts.reform.cosapi.edgecase.model.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.cosapi.util.ConfigTestUtil.createCaseDataConfigBuilder;
import static uk.gov.hmcts.reform.cosapi.util.ConfigTestUtil.getEventsFrom;

@ExtendWith(MockitoExtension.class)
class UpdateCaseEventTest {

    @InjectMocks
    private UpdateCaseEvent updateCaseEvent;

//    @Test
//    void shouldAddConfigurationToConfigBuilder() {
//        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
//
//        updateCaseEvent.configure(configBuilder);
//
//        assertThat(getEventsFrom(configBuilder).values())
//            .extracting(Event::getId)
//            .contains(UPDATE_CASE_EVENT_ID);
//        assertThat(getEventsFrom(configBuilder).values())
//            .extracting(Event::getDescription)
//            .contains("Edge case application update");
//        assertThat(getEventsFrom(configBuilder).values())
//            .extracting(Event::getName)
//            .contains("Edge case");
//    }
}
