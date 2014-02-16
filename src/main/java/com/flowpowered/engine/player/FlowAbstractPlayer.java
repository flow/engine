/*
 * This file is part of Flow Engine, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.flowpowered.engine.player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.flowpowered.api.geo.discrete.TransformProvider;
import com.flowpowered.api.input.InputSnapshot;
import com.flowpowered.api.player.AbstractPlayer;
import com.flowpowered.api.player.PlayerNetwork;
import com.flowpowered.chat.ChatReceiver;
import com.flowpowered.commands.CommandException;
import com.flowpowered.engine.network.FlowSession;
import com.flowpowered.permissions.PermissionDomain;

public abstract class FlowAbstractPlayer implements AbstractPlayer {
    protected final String name;
    protected final PlayerNetwork network;
    protected volatile TransformProvider transformProvider = TransformProvider.NullTransformProvider.INSTANCE;
    private volatile List<InputSnapshot> input = new LinkedList<>();

    public FlowAbstractPlayer(FlowSession session, String name) {
        this.name = name;
        this.network = new PlayerNetwork(session, this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean hasPermission(String permission) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasPermission(String permission, PermissionDomain domain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isInGroup(String group) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isInGroup(String group, PermissionDomain domain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> getGroups() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> getGroups(PermissionDomain domain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PlayerNetwork getNetwork() {
        return network;
    }

    @Override
    public void processCommand(String commandLine) throws CommandException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendCommand(String command, String... args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendMessage(ChatReceiver from, String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendMessage(String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendMessageRaw(String message, String type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TransformProvider getTransformProvider() {
        return transformProvider;
    }

    @Override
    public void setTransformProvider(TransformProvider provider) {
        this.transformProvider = provider == null ? TransformProvider.NullTransformProvider.INSTANCE : provider;
    }

    @Override
    public List<InputSnapshot> getInput() {
        return Collections.unmodifiableList(input);
    }

    @Override
    public void setInput(List<InputSnapshot> inputList) {
        input = inputList;
    }
}