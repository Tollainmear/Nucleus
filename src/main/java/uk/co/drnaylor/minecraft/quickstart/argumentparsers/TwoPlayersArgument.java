package uk.co.drnaylor.minecraft.quickstart.argumentparsers;

import com.google.common.base.Preconditions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import uk.co.drnaylor.minecraft.quickstart.Util;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TwoPlayersArgument extends CommandElement {
    private final Text key;
    private final Text key2;

    public TwoPlayersArgument(@Nullable Text key,Text key2) {
        super(key);
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(key2);
        this.key = key;
        this.key2 = key2;
    }

    @Nullable
    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        return null;
    }

    @Override
    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
        String sp1 = args.next();
        Optional<String> osp2 = args.nextIfPresent();

        if (!osp2.isPresent()) {
            throw args.createError(Text.of(TextColors.RED, Util.messageBundle.getString("args.twoplayer.notenough")));
        }

        String sp2 = osp2.get();
        context.putArg(key.toPlain(), Sponge.getServer().getPlayer(sp1).orElseThrow(() -> args.createError(Text.of(TextColors.RED, Util.getMessageWithFormat("args.twoplayer.noexist", sp1)))));
        context.putArg(key2.toPlain(), Sponge.getServer().getPlayer(sp2).orElseThrow(() -> args.createError(Text.of(TextColors.RED, Util.getMessageWithFormat("args.twoplayer.noexist", sp2)))));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        try {
            String s = args.peek().toLowerCase();
            return Sponge.getServer().getOnlinePlayers().stream().filter(x -> x.getName().startsWith(s.toLowerCase())).map(User::getName).collect(Collectors.toList());
        } catch (ArgumentParseException e) {
            return Sponge.getServer().getOnlinePlayers().stream().map(User::getName).collect(Collectors.toList());
        }
    }
}
