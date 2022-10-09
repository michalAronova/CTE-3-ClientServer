package DTO.team;

public class Team {
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
