package cc.sfclub.blog;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
public class little extends JavaPlugin implements Listener{
	public FileConfiguration load(File file){
        if (!(file.exists())) {
         try
        {
           file.createNewFile();
          }
         catch(IOException   e)
           {
                 e.printStackTrace();
           }
        }
        return YamlConfiguration.loadConfiguration(file);
}
	final File temp_$=new File(getDataFolder(),"playerData.yml");
	final protected FileConfiguration data = load(temp_$);
	final String Prefix= ChatColor.AQUA+""+ChatColor.BOLD+"TheLocation "+ChatColor.GOLD+">> "+ChatColor.GREEN;
	final int Label=2;
	protected HashMap<String, HashMap<String,Boolean>> inviteMap = new HashMap<>();
	protected HashMap<String,String> JoinMap = new HashMap<>();
	@Override
	public void onEnable(){

		Bukkit.getServer().getPluginManager().registerEvents(new event(), this);
		if(!getDataFolder().exists()) {
			  getDataFolder().mkdir();
			}
			File file=new File(getDataFolder(),"config.yml");
			if (!(file.exists())) {saveDefaultConfig();saveResource("playerData.yml",false);}

			reloadConfig();
		msg("Loaded.");
	}
	@Override
	public void onDisable(){
		msg("Disabling.");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if (cmd.getName().equalsIgnoreCase("tl") && args.length >= Label) {
	    	if(!(sender instanceof Player)){
			msg("Ignored.");
			//Player
			}else{
	    		Player Target=(Player)sender;
	    		switch (args[1]){
					default:
						Target.sendMessage("----------------------------------------");
						Target.sendMessage("可用的指令列表:");
						if(Target.hasPermission("TheLocation.broadcast")){

							Target.sendMessage("/tl send --发送自己坐标");
						}if(Target.hasPermission("TheLocation.team.base")){

							Target.sendMessage("/tl team --输出队伍帮助");
						}if(Target.hasPermission("TheLocation.map")) {

							Target.sendMessage("/tl map <玩家> --追踪玩家坐标");
						}if(Target.hasPermission("TheLocation.getpos")){

							Target.sendMessage("/tl get <玩家> --获取玩家坐标");
						}
						break;

					case "team":
						if(args.length==1) {
							Target.sendMessage("----------------------------------------");
							Target.sendMessage("可用的指令列表:");
							if (Target.hasPermission("TheLocation.team.base")) {
								Target.sendMessage("/tl team invite <玩家> --邀请玩家加入队伍");
								Target.sendMessage("/tl team accept <玩家> --接受邀请");
								Target.sendMessage("/tl team leave <队伍名> --移除/离开队伍");
								Target.sendMessage("/tl team create <队伍名> --创建队伍");
								Target.sendMessage("/tl team kick <玩家> --从你的队伍踢出玩家");
							}
						}else if(args.length>=2){//处理
							switch(args[2]){
								case "invite":
									if(data.getBoolean(Target.getName()+".hasTeam")==true){
									if(Bukkit.getPlayer(args[3])!=null){
										inviteMap.
												put(args[3],new HashMap<String,Boolean>());
										inviteMap.get(args[3]).put(Target.getName(),false);
										Bukkit.getPlayer(args[3]).sendMessage(Prefix+Target.getName()+
												" 邀请你加入他的小组.如果不愿意请无视这条消息。");
										Bukkit.getPlayer(args[3]).spigot()
										.sendMessage(new ComponentBuilder(Prefix+"点击 >> 我 << 同意这个请求")
										.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tl team join "+Target.getName()))
										//.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,ChatColor.AQUA+"点击同意"))
										.create());
									}else{
										Target.sendMessage(Prefix+""+ChatColor.RED+"对方不在线");
									}
								}else{
									Target.sendMessage(Prefix+""+ChatColor.RED+"你还没有队伍!");
								}
								break;
								case "accept":
									if(inviteMap.get(Target.getName())!=null && data.getBoolean(Target.getName()+".hasTeam")==false && inviteMap.get(Target.getName()).get(args[3])==false) {
										data.set("Team",args[3]);
										Target.sendMessage(Prefix + "成功加入了 " + args[3]+" 的队伍!");
										inviteMap.remove(Target.getName());
									}else{
										Target.sendMessage(Prefix+"你未接到任何队伍的邀请或你已经加入了某个队伍。");
									}
									break;
								case "leave":
									if(args[3]=="true"){
									data.set(Target.getName()+".hasTeam",false);
									}else{
									Target.spigot()
											.sendMessage(new ComponentBuilder(Prefix+ChatColor.RED+"点击我确定退出")
													.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tl team leave true"))
													//.event(new HoverEvent((HoverEvent.Action.SHOW_TEXT,ChatColor.AQUA+"三思而后行！"))
													.create());
									}
									break;
								case "create":
									data.set(Target.getName()+".hasTeam",false);
									Target.sendMessage(Prefix+"已经离开队伍..");
									break;
									}}else{
									Target.sendMessage(Prefix+"指令输入错误。");
									}
								case "kick":
									data.set(args[3]+".Team","null");
										data.set(args[3]+".hasTeam","false");
										Target.sendMessage(Prefix+"成功踢出");
									if (!(Bukkit.getPlayer(args[3])==null)) {
										Bukkit.getPlayer(args[3]).sendMessage(Prefix+"你被移出队伍");
									}
									break;
					case "send":
						if(Target.hasPermission("TheLocation.broadcast")){
							//getLoc
							double X=Target.getLocation().getX();
							double Y=Target.getLocation().getY();
							double Z=Target.getLocation().getZ();
							Bukkit.broadcastMessage(getConfig().getString("broadcast")
									.replaceAll("%x",X+"")
									.replaceAll("%y",Y+"")
									.replaceAll("%z",Z+"")
									.replaceAll("%p",Target.getName())
									.replaceAll("%w",Target.getWorld().getName())
									.replaceAll("&","§")
							);
							Target.sendMessage(getConfig().getString("bc_Success")
									.replaceAll("&","§")
									.replaceAll("%p",Target.getName())
							);
						}
						break;
					case "get":
						if(Bukkit.getPlayer(args[2])!=null) {
							if (Target.hasPermission("TheLocation.getpos")) {
								if (Bukkit.getPlayer(args[2]) != null) {
									Player a=Bukkit.getPlayer(args[2]);
									Target.spigot()
											.sendMessage(new ComponentBuilder(Prefix + "")
							.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/minecraft:tp "+a.getLocation().getX()+" "+a.getLocation().getY()+" "+a.getLocation().getZ()))
													//.event(new HoverEvent((HoverEvent.Action.SHOW_TEXT,ChatColor.AQUA+"三思而后行！"))
													.create());
					Target.sendMessage(Prefix+"Success!");
							} else {//自己
							Target.sendMessage(Prefix+"你的坐标:X:"
									+Target.getLocation().getX()+" Y:"
									+Target.getLocation().getY()+" Z:"
									+Target.getLocation().getZ()+" 世界:"
									+Target.getWorld().getName());
								}
						}
						} else {
							Target.sendMessage(Prefix + "该玩家不存在/不在服务器里");
						}
						break;
	    		}
			}//end
	    	return true;
	    	}
		return false;
	}
	void msg(String message){
		getServer().getConsoleSender().sendMessage(Prefix+message);
	}
	public void writeConfig(String a,String b){
		data.set(a,b);
	}

}
