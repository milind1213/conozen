package com.convozen.Pages.Playwrights;

import com.convozen.CommonUtils.CommonPlaywright;
import com.microsoft.playwright.Page;

public class WebDashboard extends CommonPlaywright {
    public Page page;
    public final ConvozenAgents agentPage;
    public final ConvozenCalls callsPage;
    public final ConvozenMoments momentsPage;
    public final ConvozenChecklists checklistsPage;
	public final WebLogin webLogin;

    public WebDashboard(Page page) {
        super(page);
        this.page = page;
        agentPage = new ConvozenAgents(page);
        callsPage = new ConvozenCalls(page);
        momentsPage = new ConvozenMoments(page);
        checklistsPage = new ConvozenChecklists(page);
		webLogin = new WebLogin(page);
    }

	public WebLogin getWebLogins(){
		return webLogin;
	}

    public ConvozenAgents getAgentsPage() {
        return agentPage;
    }

    public ConvozenCalls getCallPage() {
        return callsPage;
    }

    public ConvozenMoments getMomentPage() {
        return momentsPage;
    }

    public ConvozenChecklists getChecklistPage() {
        return checklistsPage;
    }


}
