package edu.csf;

import edu.csf.controller.*;
import edu.csf.domain.Agency;
import edu.csf.domain.Date;
import edu.csf.domain.Player;
import edu.csf.reflection.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import static edu.csf.domain.Config.*;

public class Main {

    private static final DateController dateController;
    private static final PlayerController playerController;
    private static final AgencyController agencyController;

    static {
        try {
            dateController = ApplicationContext.getInstance(DateController.class);
            playerController = ApplicationContext.getInstance(PlayerController.class);
            agencyController = ApplicationContext.getInstance(AgencyController.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Date date = null;
        Player player = null;
        Player bot = null;
        Agency agency = null;
        boolean isStarted = false;
        while (!isStarted) {
            System.out.println("1. Start game");
            System.out.println("2. Continue game");
            int i = scanner.nextInt();
            switch (i) {
                case 1:
                    date = new Date();
                    player = new Player(date);
                    bot = new Player(date);
                    agency = new Agency();
                    agency.addPlayer(player);
                    agency.addPlayer(bot);
                    isStarted = true;
                    break;
                case 2:
                    var dateOptional= dateController.get(1L);
                    if (dateOptional.isPresent()) {
                        date = dateOptional.get();
                    } else {
                        System.out.println("Can't load game");
                        break;
                    }
                    var playerOptional= playerController.get(1L);
                    if (playerOptional.isPresent()) {
                        player = playerOptional.get();
                    } else {
                        System.out.println("Can't load game");
                        break;
                    }
                    var botOptional= playerController.get(2L);
                    if (botOptional.isPresent()) {
                        bot = botOptional.get();
                    } else {
                        System.out.println("Can't load game");
                        break;
                    }
                    var agencyOptional= agencyController.get(1L);
                    if (agencyOptional.isPresent()) {
                        agency = agencyOptional.get();
                    } else {
                        System.out.println("Can't load game");
                        break;
                    }
                    isStarted = true;
                    break;
                default:
                    System.out.println("Try again");
                    break;
            }
        }
        int i;
        while(!date.isEnd()) {
            boolean isNextTurn = false;
            while(!isNextTurn) {
                System.out.printf("Money: %s\n", player.getMoney());
                System.out.printf("Estimate spending: %s\n", player.getEstimateSpending());
                System.out.printf("Next month spending: %s\n", player.getEstimateSpendingPerMonth());
                System.out.println("Actions:");
                System.out.println("1. Build building");
                System.out.println("2. Put up apartments for sale");
                System.out.println("3. Buy advertising");
                System.out.println("4. Set square meter price");
                System.out.println("5. See player statistic");
                System.out.println("6. See bot statistic");
                System.out.println("7. Next turn");
                System.out.println("8. Save and exit");
                i = scanner.nextInt();
                switch (i) {
                    case 1:
                        System.out.println("Select building type");
                        System.out.printf("1. House (Price: %s, BuildTime: %s)\n", priceHouse, monthHouseBuilding);
                        System.out.printf("2. Store (Price: %s, BuildTime: %s)\n", priceStore, monthStoreBuilding);
                        i = scanner.nextInt();
                        switch (i) {
                            case 1:
                                player.buildHouse();
                                break;
                            case 2:
                                player.buildStore();
                                break;
                            default:
                                break;
                        }
                        break;
                    case 2:
                        System.out.println("Apartment count to sell");
                        i = scanner.nextInt();
                        System.out.printf("Month limit (max %s)\n", monthHouseBuilding);
                        int j = scanner.nextInt();
                        player.putUpForSale(i, j);
                        break;
                    case 3:
                        System.out.println("Select advertising type");
                        System.out.printf("1. Demand (%s per 1000)\n", increasingHouse);
                        System.out.printf("2. Sales (%s per 1000)\n", increasingStore);
                        i = scanner.nextInt();
                        System.out.println("Money to spend");
                        double money = scanner.nextDouble();
                        switch (i) {
                            case 1:
                                player.buyDemandAdvertising(money);
                                break;
                            case 2:
                                player.buySalesAdvertising(money);
                                break;
                            default:
                                break;
                        }
                        break;

                    case 4:
                        System.out.println("Square meter price");
                        double price = scanner.nextDouble();
                        player.setSquareMeterPrice(price);
                    case 5:
                        System.out.printf("Capital: %s\n", player.getCapital());
                        System.out.printf("Monthly sales: %s\n", player.getMonthSales());
                        System.out.printf("No for sale apartment count: %s\n", player.getNoForSaleCount());
                        System.out.printf("For sale apartment count: %s\n", player.getForSaleCount());
                        System.out.printf("Sold apartment count: %s\n", player.getSoldCount());
                        System.out.printf("Percent demand increase: %s\n", player.getPercentDemand());
                        System.out.printf("Percent sales increase: %s\n", player.getPercentSales());
                        System.out.printf("House count: %s\n", player.getHouseCount());
                        System.out.printf("Store count: %s\n", player.getStoreCount());
                        System.out.printf("House in building count: %s\n", player.getUnbuiltHouseCount());
                        System.out.printf("Store in building count: %s\n", player.getUnbuiltStoreCount());
                        break;
                    case 6:
                        System.out.printf("Money: %s\n", bot.getMoney());
                        System.out.printf("Estimate spending: %s\n", bot.getEstimateSpending());
                        System.out.printf("Next month spending: %s\n", bot.getEstimateSpendingPerMonth());
                        System.out.printf("Capital: %s\n", bot.getCapital());
                        System.out.printf("Monthly sales: %s\n", bot.getMonthSales());
                        System.out.printf("No for sale apartment count: %s\n", bot.getNoForSaleCount());
                        System.out.printf("For sale apartment count: %s\n", bot.getForSaleCount());
                        System.out.printf("Sold apartment count: %s\n", bot.getSoldCount());
                        System.out.printf("Percent demand increase: %s\n", bot.getPercentDemand());
                        System.out.printf("Percent sales increase: %s\n", bot.getPercentSales());
                        System.out.printf("House count: %s\n", bot.getHouseCount());
                        System.out.printf("Store count: %s\n", bot.getStoreCount());
                        System.out.printf("House in building count: %s\n", bot.getUnbuiltHouseCount());
                        System.out.printf("Store in building count: %s\n", bot.getUnbuiltStoreCount());
                        break;
                    case 7:
                        isNextTurn = true;
                        break;
                    case 8:
                        dateController.put(date);
                        playerController.put(player);
                        playerController.put(bot);
                        agencyController.put(agency);
                        return;
                    default:
                        System.out.println("Try again");
                        break;
                }
            }
            while (bot.getMoney() - bot.getEstimateSpending() > priceHouse) {
                bot.buildHouse();
            }
            bot.putUpForSale(bot.getNoForSaleCount(), monthHouseBuilding);
            player.iterate();
            bot.iterate();
            agency.iterate();
            date.incrementMonth();
        }
    }
}