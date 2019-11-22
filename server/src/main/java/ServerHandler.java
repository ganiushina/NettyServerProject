import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    String nickName;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
                if (Files.exists(Paths.get("server_storage/" + nickName + "/"+ fr.getFilename()))) {
                    FileMessage fm = new FileMessage(Paths.get("server_storage/" + nickName + "/" + fr.getFilename()));
                    ctx.writeAndFlush(fm);
                }

            } else
            if (msg instanceof FileListRequest) {
                {
                    FileListRequest flr = (FileListRequest) msg;
                    boolean success = (
                            new File("server_storage/" + nickName)).mkdir();
                    if (success) {
                        System.out.println("Directory: "
                                + nickName + " created");
                    }

                    if (flr.getAction().equals("send")){
                        Files.write(Paths.get("server_storage/" + nickName + "/" + flr.getFileMessage().getFilename()), flr.getFileMessage().getData(), StandardOpenOption.CREATE);
                    }

                    List<String> filesName = new ArrayList<>();
                    if (flr.getAction().equals("del")) {
                        Files.delete(Paths.get("server_storage/" + nickName + "/" + flr.getFileName()));
                    }
                    try (Stream<Path> paths = Files.walk(Paths.get("server_storage/" + nickName + "/"))) {
                        paths
                                .filter(Files::isRegularFile)
                                //  .forEach(System.out::println);
                                .forEach(o -> {
                                    filesName.add(o.getFileName().toString());
                                });
                    }

                    flr.setFilenameList(filesName);
                    ctx.writeAndFlush(flr);
                }
            } else

            if (msg instanceof AuthRequest) {
                AuthRequest ar = (AuthRequest) msg;
                AuthService.connect();
                nickName = AuthService.getNickByLoginAndPass(ar.getLogin(), ar.getPassword());
                if (nickName != null) {
                    ar.setNickName(nickName);
                    ar.setAuth(true);
                }
                ctx.writeAndFlush(ar);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        AuthService.disconnect();
        ctx.close();
    }
}
