package zhscraft.DarkenHome;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CommandMethods {

	public static void help(Player p, String arg2, String[] a) {
		if (a[0].equalsIgnoreCase("help")) {
			p.sendMessage(ChatColor.GREEN + "/home | takes you to you're home");
			p.sendMessage(ChatColor.GREEN + "/home <player> | takes you to a players home");
			p.sendMessage(ChatColor.GREEN + "/myhome set | sets you're home");
			p.sendMessage(ChatColor.GREEN + "/myhome invite <player> | invites a player to you're home");
			p.sendMessage(ChatColor.GREEN + "/myhome remove | removes you're homeset");
			p.sendMessage(ChatColor.GREEN + "/myhome clear | clears the invite list for you're homeset");
			if (p.hasPermission("darken.home.admin")) {
				p.sendMessage(ChatColor.GREEN + "/myhome admin delete <player> | delete players home");
			}
		}
	}

	public static void set(Player p, String arg2, String[] a) {
		if (a[0].equalsIgnoreCase("set")) {
			Location loc = p.getLocation();
			Vector vec = new Vector(loc.getX(), loc.getY(), loc.getZ());
			try {
				if (sqlHandler.checkDatabase_homeset("homeset", p.getUniqueId().toString()) == true) {
					sqlHandler.setHomeset(p.getUniqueId().toString(), "" + vec.getX(), "" + vec.getY(), "" + vec.getZ(), false, true);
					p.sendMessage(ChatColor.AQUA + "you're home has been updated :)");
				} else {
					sqlHandler.setHomeset(p.getUniqueId().toString(), "" + vec.getX(), "" + vec.getY(), "" + vec.getZ(), false, false);
					p.sendMessage(ChatColor.AQUA + "Welcome to you're new home :)");
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void invite(Player p, String arg2, String[] a) {
		if (a[0].equalsIgnoreCase("invite")) {

			String input = Bukkit.getOfflinePlayer(a[1]).getUniqueId().toString();
			String MyUUID = p.getUniqueId().toString();
			String UUIDtoName = Bukkit.getOfflinePlayer(UUID.fromString(input)).getName();

			sqlHandler.addInvited(MyUUID, input);
			p.sendMessage(ChatColor.DARK_GRAY + "You have invited " + UUIDtoName);

			try {
				if (Bukkit.getPlayer(UUID.fromString(input)).isOnline()) {
					Bukkit.getPlayer(a[1]).sendMessage(ChatColor.DARK_GRAY + p.getName() + " has invited you to there home");
				}
			} catch (NullPointerException ex) {

			}

		}
	}

	public static void clear(Player p, String arg2, String[] a) {
		if (a[0].equalsIgnoreCase("clear")) {
			String MyUUID = p.getUniqueId().toString();
			sqlHandler.removePlayerInvite(MyUUID);
			p.sendMessage(ChatColor.DARK_GRAY + "you have cleared you're invite list!");

		}
	}

	public static void remove(Player p, String arg2, String[] a) {
		if (a[0].equalsIgnoreCase("remove")) {
			String MyUUID = p.getUniqueId().toString();
			sqlHandler.removePlayerByUUID(MyUUID);
			p.sendMessage(ChatColor.DARK_GRAY + "you have delete you're home");
		}
	}
	
	public static void removeinvited(Player p, String arg2, String[] a){
		if(a[0].equalsIgnoreCase("uninvite")){
			String RemovedPlayer = Bukkit.getOfflinePlayer(a[1]).getUniqueId().toString();
			String MyUUID = p.getUniqueId().toString();
			sqlHandler.removeInvitedPlayer(RemovedPlayer, MyUUID);
			p.sendMessage("Player was removed from you invite list");
			
		}
	}

	public static void admin(Player p, String arg2, String[] a) {
		if (a[0].equalsIgnoreCase("admin")) {

			if (a[1].contains("delete")) {
				String input = Bukkit.getOfflinePlayer(a[2]).getUniqueId().toString();
				String UUIDtoName = Bukkit.getOfflinePlayer(UUID.fromString(input)).getName();

				sqlHandler.removePlayerByUUID(input);
				p.sendMessage(ChatColor.DARK_GRAY + "you have delete " + UUIDtoName + " home");
			}

		}
	}

}
