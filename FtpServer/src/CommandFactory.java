public class CommandFactory {

    public static Command createCommand(String type) {

        type = type.toUpperCase();
        switch(type)
        {
            case "USER":return new UserCommand();

            case "PASS":return new PassCommand();

            case "LIST":return new DirCommand();

            case "MYPORT":return new PortCommand();

            case "CLOSE":return new QuitCommand();

            case "RETR":return new RetrCommand();

            case "CWD":return new CwdCommand();

            case "STOR":return new StoreCommand();

            case "REGISTER" : return new RegisterCommand();

            default :return null;
        }

    }
}