package hypertjs.quickstats;

import org.bukkit.plugin.java.JavaPlugin;

public class QuickStats extends JavaPlugin 
{
	
    @Override
    public void onEnable() 
    {
    	System.out.println("[QuickStats] Plugin created by hypertjs.");
    	this.getCommand("stats").setExecutor(new CommandStats());
    }
    
    @Override
    public void onDisable() 
    {

    }
}