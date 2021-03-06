package zhscraft.DarkenHome;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Home implements CommandExecutor {

	Client c;

	public Home(Client c) {
		this.c = c;
	}

	public boolean onCommand(CommandSender pa, Command arg1, String arg2, String[] a) {
		Player p = (Player) pa;
		UUID i = p.getUniqueId();

		if (arg2.equalsIgnoreCase("home") && a.length == 0) {
			if (p.hasPermission("darken.home.user")) {
				try {
					if (sqlHandler.checkDatabase_homeset("homeset", p.getUniqueId().toString()) == true) {
						Vector v = sqlHandler.GetHomeCoords(p.getUniqueId().toString());
						p.teleport(new Location(p.getWorld(), v.getX(), v.getY(), v.getZ()));
						p.sendMessage(ChatColor.DARK_GRAY + "Welcome to you're home");
					} else {
						p.sendMessage(ChatColor.DARK_GRAY + "you do not have a home use '/myhome set' to setup you're homeset");
					}

				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			} else {
				p.sendMessage("you do not have permissions to use this command");
			}

		} else if (arg2.equalsIgnoreCase("home") && a.length > 0) {
			if (p.hasPermission("darken.home.user") || p.hasPermission("darken.home.guest")) {
				try {
					if (Bukkit.getOfflinePlayer(a[0]) != null || Bukkit.getPlayer(a[0]) != null) {
						if (a[0].contains(Bukkit.getOfflinePlayer(a[0]).getName()) || a[0].contains(Bukkit.getPlayer(a[0]).getName())) {

							String CheckOtherPlayerUUID = UUIDStorageHandler.getUUIDFromUsername(a[0]);
							String OwnerName = Bukkit.getOfflinePlayer(UUID.fromString(CheckOtherPlayerUUID)).getName();

							if (sqlHandler.isPublic(CheckOtherPlayerUUID) == false) {
								if (InvitedCheckHandler.isInivted(CheckOtherPlayerUUID, p.getUniqueId().toString()) == true) {
									Vector v = sqlHandler.GetHomeCoords(Bukkit.getOfflinePlayer(CheckOtherPlayerUUID).getName());
									p.teleport(new Location(p.getWorld(), v.getX(), v.getY(), v.getZ()));
									p.sendMessage(ChatColor.DARK_GRAY + "Welcome to " + OwnerName + " home");
								} else {
									p.sendMessage(ChatColor.DARK_GRAY + "you are not invited to this players home sorry :(");
								}
							} else {
								Vector v = sqlHandler.GetHomeCoords(Bukkit.getOfflinePlayer(CheckOtherPlayerUUID).getName());
								p.teleport(new Location(p.getWorld(), v.getX(), v.getY(), v.getZ()));
								p.sendMessage(ChatColor.DARK_GRAY + "Welcome to " + OwnerName + " home");
							}

						}

					}

				} catch (NullPointerException e) {
					p.sendMessage(ChatColor.DARK_GRAY + "check the username and try again!");
				}
			} else {
				p.sendMessage("you do not have permissions to use this command");
			}
		}

		return false;
	}
}
