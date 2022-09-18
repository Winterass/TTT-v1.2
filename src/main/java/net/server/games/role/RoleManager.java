package net.server.games.role;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import net.server.games.Main;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RoleManager {

    private Main plugin;
    private HashMap<String, Role> playerRoles;
    private ArrayList<Player> players;
    private ArrayList<String> traitorPlayers;

    private int traitors, detectives, innocents;

    public RoleManager(Main plugin) {
        this.plugin = plugin;
        playerRoles = new HashMap<>();
        players = plugin.getPlayers();
        traitorPlayers = new ArrayList<>();
    }

    public void calculateRoles() {
        int playerSize = players.size();

        traitors = (int) Math.round(Math.log(playerSize) * 1.2);
        detectives = (int) Math.round(Math.log(playerSize) * 0.75);
        innocents = playerSize - traitors - detectives;

        System.out.println("Traitor: " + traitors);
        System.out.println("Detectives: " + detectives);
        System.out.println("Innocents: " + innocents);

        Collections.shuffle(players);


        int counter = 0;
        for(int i = counter; i < traitors; i++) {
            playerRoles.put(players.get(i).getName(), Role.TRAITOR);
            traitorPlayers.add(players.get(i).getName());
        }
        counter += traitors;
        for(int i = counter; i < detectives + counter; i++) {
            playerRoles.put(players.get(i).getName(), Role.DETECTIVE);
        }
        counter += detectives;
        for(int i = counter; i < innocents + counter; i++) {
            playerRoles.put(players.get(i).getName(), Role.INNOCENT);
        }
        for(Player current : players) {
            switch (getPlayerRole(current)) {
                case TRAITOR:
                    for(Player others : players) {
                        setFakeArmor(others, current.getEntityId(), (getPlayerRole(others) != Role.TRAITOR) ? Color.GREEN : Color.RED);
                    }
                    break;
                case DETECTIVE:
                    setArmor(current, Color.BLUE);
                    break;
                case INNOCENT:
                    setArmor(current, Color.GREEN);
                    break;

                default:
                    break;
            }
        }
    }

    public void setArmor(Player player, Color color) {
        player.getInventory().setChestplate(getColoredChestplate(color));
    }

    public void setFakeArmor(Player player, int entityID, Color color) {
        ItemStack armor = getColoredChestplate(color);

        final List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R3.ItemStack>> equipmentList = new ArrayList<>();

        equipmentList.add(new Pair<>(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(armor)));

        final PacketPlayOutEntityEquipment entityEquipment = new PacketPlayOutEntityEquipment(entityID, equipmentList);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(entityEquipment);

    }

    private ItemStack getColoredChestplate(Color color) {
        ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta armorMeta = (LeatherArmorMeta) armor.getItemMeta();
        armorMeta.setColor(color);
        armor.setItemMeta(armorMeta);
        return armor;
    }

    public Role getPlayerRole(Player player) {
        return playerRoles.get(player.getName());
    }

    public ArrayList<String> getTraitorPlayers() {
        return traitorPlayers;
    }

}
