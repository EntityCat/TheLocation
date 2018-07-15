package cc.sfclub.blog;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;


public class event implements Listener{
    little i = new little();
    @EventHandler
    public void onJoin(PlayerJoinEvent evt){
         if(!(evt.getPlayer().hasPlayedBefore())){
            i.data.set(evt.getPlayer().getName()+".hasTeam",false);
            i.data.set(evt.getPlayer().getName()+".Team","null");
         }


    }
}
