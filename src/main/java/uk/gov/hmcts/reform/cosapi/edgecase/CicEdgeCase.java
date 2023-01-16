package uk.gov.hmcts.reform.cosapi.edgecase;

//@Component
public class CicEdgeCase
    //implements CCDConfig<CaseData, State, UserRole>
    {

        // Temporarily disabling this configuration as it conflicts with
        // what we have configured for sptribs-case-api.

//    @Autowired
//    AppsConfig appsConfig;
//
//    @Override
//    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
//        configBuilder.setCallbackHost(System.getenv().getOrDefault("CASE_API_URL",
//                                                                   "http://localhost:4550"));
//        configBuilder.caseType(CommonConstants.ST_CIC_CASE_TYPE, "New edge case", "Handling of edge cases");
//        configBuilder.jurisdiction(CommonConstants.ST_CIC_JURISDICTION,
//                                   "ST jurisdiction CIC", "edge-cases");
//        configBuilder.grant(State.DRAFT, Permissions.CREATE_READ_UPDATE, UserRole.CITIZEN);
//        configBuilder.event("");
//    }
}

