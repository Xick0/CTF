package dev.marco.xicko.Player;

import dev.marco.xicko.Collections.ListasIterador.Classes.ArrayUnorderedList;
import dev.marco.xicko.Bot.Bot;
import dev.marco.xicko.Collections.ListasIterador.Classes.OrderedLinkedList;
import dev.marco.xicko.Collections.ListasIterador.Interfaces.OrderedListADT;
import dev.marco.xicko.Collections.ListasIterador.Interfaces.UnorderedListADT;
import dev.marco.xicko.Collections.Pesquisas_Sorts.Classes.UnorderedLinkedList;
import dev.marco.xicko.Collections.Queue.ArrayQueue;

public class Player{
    private String name;
    private UnorderedListADT<Bot> bots;
    private ArrayUnorderedList<String> botPositions;

    private int flag;

    public Player(String name) {
        this.name = name;
        this.bots = new ArrayUnorderedList<Bot>();
    }
    public void addBot(Bot bot){
        bots.addToRear(bot);
    }
    public void removeBot(Bot bot){
        bots.remove(bot);
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnorderedListADT<Bot> getBots() {
        return bots;
    }
}