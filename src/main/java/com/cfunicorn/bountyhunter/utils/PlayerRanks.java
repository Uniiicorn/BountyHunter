package com.cfunicorn.bountyhunter.utils;

public enum PlayerRanks {

    RANK1("Novice Pursuer","♦", 0),
    RANK2("Bounty Rookie","♦♦", 3),
    RANK3("Skilled Tracker","♦♦♦", 5),
    RANK4("Seasoned Hunter","✦", 8),
    RANK5("Elite Pursuer","✦✦", 10),
    RANK6("Master Bounty Hunter","✦✦✦", 15),
    RANK7("Grand Tracker","★", 20),
    RANK8("Legendary Pursuer","★★", 30),
    RANK9("Supreme Bounty Hunter","★★★", 40),
    RANK10("Ultimate Enforcer","♛", 50);

    PlayerRanks(String name, String icon, int bonus) {
        setName(name);
        setIcon(icon);
        setBonus(bonus);
    }

    String name, icon;
    int bonus;

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}
