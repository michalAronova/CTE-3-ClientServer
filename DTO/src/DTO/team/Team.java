package DTO.team;

import java.io.Serializable;

public class Team implements Serializable {
   private final String name;
   private final int agentAmount;
   private final int missionSize;

   public Team(String name, int agentAmount, int missionSize) {
      this.name = name;
      this.agentAmount = agentAmount;
      this.missionSize = missionSize;
   }

   public String getName() {
      return name;
   }

   public int getAgentAmount() {
      return agentAmount;
   }

   public int getMissionSize() {
      return missionSize;
   }
}
