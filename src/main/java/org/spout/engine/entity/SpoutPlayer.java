package org.spout.engine.entity;

import java.util.List;
import java.util.Set;

import com.flowpowered.permissions.PermissionDomain;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.flow.commands.CommandException;
import org.spout.flow.commands.CommandSender;

public class SpoutPlayer implements Player {

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDisplayName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDisplayName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isOnline() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasJoinedBefore() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void kick() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void kick(String reason) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void ban() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void ban(boolean kick) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void ban(boolean kick, String reason) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean save() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setVisible(Entity entity, boolean visible) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Entity> getInvisibleEntities() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isInvisible(Entity entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendCommand(String command, String... args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Entity getEntity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendMessage(String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendMessage(CommandSender from, String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendMessageRaw(String message, String type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void processCommand(String commandLine) throws CommandException {
        throw new UnsupportedOperationException("Not supported yet.");
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

}
