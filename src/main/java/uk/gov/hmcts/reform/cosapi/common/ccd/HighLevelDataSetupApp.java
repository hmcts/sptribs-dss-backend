package uk.gov.hmcts.reform.cosapi.common.ccd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.befta.dse.ccd.CcdEnvironment;
import uk.gov.hmcts.befta.dse.ccd.CcdRoleConfig;
import uk.gov.hmcts.befta.dse.ccd.DataLoaderToDefinitionStore;

import java.util.List;
import java.util.Locale;

public class HighLevelDataSetupApp extends DataLoaderToDefinitionStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(HighLevelDataSetupApp.class);

    public static final String PUBLIC = "PUBLIC";
    private static final CcdRoleConfig[] CCD_ROLES_NEEDED_FOR_ADOPTION = {
        new CcdRoleConfig("citizen", PUBLIC),
        new CcdRoleConfig("caseworker-privatelaw", PUBLIC),
        new CcdRoleConfig("caseworker-privatelaw-courtadmin", PUBLIC),
        new CcdRoleConfig("caseworker-privatelaw-solicitor", PUBLIC)
    };

    private final CcdEnvironment environment;

    public HighLevelDataSetupApp(CcdEnvironment dataSetupEnvironment) {
        super(dataSetupEnvironment);
        environment = dataSetupEnvironment;
    }

    public static void main(String[] args) throws Throwable {
        if (CcdEnvironment.valueOf(args[0].toUpperCase(Locale.UK)).equals(CcdEnvironment.PROD)) {
            return;
        }
        main(HighLevelDataSetupApp.class, args);
    }

    @Override
    protected boolean shouldTolerateDataSetupFailure() {
        return true;
    }

    @Override
    public void addCcdRoles() {
        for (CcdRoleConfig roleConfig : CCD_ROLES_NEEDED_FOR_ADOPTION) {
            try {
                LOGGER.info("\n\nAdding CCD Role {}.", roleConfig);
                addCcdRole(roleConfig);
                LOGGER.info("\n\nAdded CCD Role {}.", roleConfig);
            } catch (Exception e) {
                LOGGER.error("\n\nCouldn't add CCD Role {} - Exception: {}.\n\n", roleConfig, e);
                if (!shouldTolerateDataSetupFailure()) {
                    throw e;
                }
            }
        }
    }

    @Override
    protected List<String> getAllDefinitionFilesToLoadAt(String definitionsPath) {
        String environmentName = environment.name().toLowerCase(Locale.UK);
        return List.of(
            "build/ccd-config/ccd-" + "PRLAPPS" + "-" + environmentName + ".xlsx"
        );
    }

    @Override
    public void createRoleAssignments() {
        // Do not create role assignments.
        BeftaUtils.defaultLog("Will NOT create role assignments!");
    }
}
