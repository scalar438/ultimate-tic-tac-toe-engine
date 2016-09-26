/*
 * Copyright 2016 riddles.io (developers@riddles.io)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *     For the full copyright and license information, please view the LICENSE
 *     file that was distributed with this source code.
 */

package io.riddles.javainterface.game.player;

import io.riddles.javainterface.io.BotIOHandler;
import io.riddles.javainterface.io.BotIO;

/**
 * io.riddles.javainterface.engine.player.AbstractPlayer - Created on 2-6-16
 *
 * DO NOT EDIT THIS FILE
 *
 * Used to store basic information about the player and used to send/request
 * messages to the bot that this player represents. This Class should be
 * extended and additional information about the player can be stored there.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public abstract class AbstractPlayer {

    private String name;
    private int id;
    protected BotIO ioHandler;

    public AbstractPlayer(int id) {
        this.id = id;
        this.name = "player" + id;
        this.ioHandler = new BotIOHandler(id);
    }

    public void setIoHandler(BotIO iohandler) {
        this.ioHandler = iohandler;
    }

    /**
     * @return The id of this player
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * @param id The id of this player
     */
    public void setId(int id) { this.id = id; }

    /**
     * @return The String name of this Player
     */
    public String getName() {
        return this.name;
    }

    public BotIO getIoHandler() {
        return this.ioHandler;
    }

    /**
     * Send one setting to the player
     * @param type Setting type
     * @param value Setting value
     */
    public void sendSetting(String type, String value) {
        this.ioHandler.sendMessage(String.format("settings %s %s", type, value));
    }

    /**
     * Send one setting to the player
     * @param type Setting type
     * @param value Setting value
     */
    public void sendSetting(String type, int value) {
        this.ioHandler.sendMessage(String.format("settings %s %d", type, value));
    }

    /**
     * Sends one update to the player about another player or himself
     * @param type Type of update
     * @param player What player the update is about
     * @param value Value of the update
     */
    public void sendUpdate(String type, AbstractPlayer player, String value) {
        this.ioHandler.sendMessage(String.format("update %s %s %s", player.getName(), type, value));
    }

    /**
     * Sends one update to the player about another player or himself
     * @param type Type of update
     * @param player What player the update is about
     * @param value Value of the update
     */
    public void sendUpdate(String type, AbstractPlayer player, int value) {
        this.ioHandler.sendMessage(String.format("update %s %s %d", player.getName(), type, value));
    }

    /**
     * Sends one update to the player about the game in general, like round number
     * @param type Type of update
     * @param value Value of the update
     */
    public void sendUpdate(String type, String value) {
        this.ioHandler.sendMessage(String.format("update game %s %s", type, value));
    }

    /**
     * Sends one update to the player about the game in general, like round number
     * @param type Type of update
     * @param value Value of the update
     */
    public void sendUpdate(String type, int value) {
        this.ioHandler.sendMessage(String.format("update game %s %d", type, value));
    }

    /**
     * Send a warning about the engine to the player that will
     * be logged in the dump.
     * @param warning Warning to be logged
     */
    public void sendWarning(String warning) {
        this.ioHandler.sendWarning(String.format("Engine warning: \"%s\"", warning));
    }

    /**
     * Asks the bot for given move type and returns the answer
     * @param moveType Type of move the bot has to return
     * @return The bot's output
     */
    public String requestMove(String moveType) {
        return this.ioHandler.sendRequest(String.format("action %s", moveType));
    }
}
