package DTO.contestForAgent;

import DTO.contest.Contest;
import DTO.dmInfo.DMInfo;

public class ContestForAgent {
    private final Contest contest;
    private final DMInfo dmInfo;

    public ContestForAgent(Contest contest, DMInfo dmInfo) {
        this.contest = contest;
        this.dmInfo = dmInfo;
    }

    public Contest getContest() {
        return contest;
    }

    public DMInfo getDmInfo() {
        return dmInfo;
    }
}
