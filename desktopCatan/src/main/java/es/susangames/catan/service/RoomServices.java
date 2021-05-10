package es.susangames.catan.service;

import java.util.ArrayList;
import java.lang.reflect.Type;
import org.json.*;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import es.susangames.catan.service.ws;
import es.susangames.catan.service.UserService;

public class RoomServices {
    
    enum RoomMsgStatus {
        FOUND("FOUND"), 
        SEARCHING("SEARCHING"), 
        FAILED("UPDATED_PLAYERS"), 
        UPDATED_PLAYERS("UPDATED_PLAYERS"), 
        UPDATED_INVITES("UPDATED_INVITES"),
        CLOSED("CLOSED"), 
        CREATED("CREATED"), 
        CANCELLED("CANCELLED");

        private String _status;

        private RoomMsgStatus(String status) {
            this._status = status;
        }

        public String toString () {
            return this._status;
        }
    }

    enum InviteMsgstatus {
        INVITED, ACCEPTED, CANCELLED, FULL, QUEUING, CLOSED, OPEN
    }

    enum RoomStatus {
        SEARCHING, CREATED
    }

    public static class UserCardInfo {
        private String _username;
        private String _avatar;

        UserCardInfo (String username, String avatar) {
            this._username = username;
            this._avatar = avatar;
        }

        void setUsername (String username) {
            this._username = username;
        }

        String getUsername () {
            return this._username;
        }

        void setAvatar (String avatar) {
            this._avatar = avatar;
        }

        String getAvatar () {
            return this._avatar;
        }
    }

    public static class Room {
        private String _leader;
        private String _id;
        private ArrayList<String> _invites;
        private ArrayList<UserCardInfo> _players;
        private String _status;

        Room (String leader, String id, ArrayList<String> invites, ArrayList<UserCardInfo> players, String status) {
            this._leader = leader;
            this._id = id;
            this._invites = invites;
            this._players = players;
            this._status = status;
        }

        void setLeader (String leader) {
            this._leader = leader;
        }

        String getLeader () {
            return this._leader;
        }

        void setId (String id) {
            this._id = id;
        }

        String getId () {
            return this._id;
        }

        void setInvites (ArrayList<String> invites) {
            this._invites = invites;
        }

        ArrayList<String> getInvites () {
            return this._invites;
        }

        void setPlayers (ArrayList<UserCardInfo> players) {
            this._players = players;
        }

        ArrayList<UserCardInfo> getPlayers () {
            return this._players;
        }

        void setStatus (String status) {
            this._status = status;
        }

        String getStatus () {
            return this._status;
        }
    }

    public static class Invite {
        private String _leader;
        private String _id;

        Invite (String leader, String id) {
            this._leader = leader;
            this._id = id;
        }

        void setLeader (String leader) {
            this._leader = leader;
        }

        String getLeader () {
            return this._leader;
        }

        void setId (String id) {
            this._id = id;
        }

        String getId () {
            return this._id;
        }
    }

    // Sala
    public static Room room = null;
    public static ArrayList<Invite> invites = new ArrayList<Invite> ();
    public static Boolean buscandoPartida = false;
    public static Boolean uniendoseASala = false;
    public static Boolean creandoSala  = false;
    public static Boolean liderCerroSala = false;
    public static Boolean errorAlUnirseASala = false;

    public RoomServices () {
        
    }
    
    /**
     * Devuelve true si el usuario es el líder de la sala. Si no 
     * existe la sala o existe pero no es el líder, devuelve false. 
     */
    public static Boolean soyLider () {
        if (room != null) {
            return room.getLeader().equals(UserService.getUsername());
        }
        return false;
    }
    /**
     * Devuelve true si se inició la búsqueda de partida en la sala
     */
    public static Boolean busquedaIniciada () {
        if (room != null) {
            return room.getStatus().equals("SEARCHING");
        }
        return false;
    }

    public static void procesarMensajeCreacionSala (Object payload) {
        System.out.println(payload.toString());
        JSONObject jsObj = new JSONObject(payload.toString());
        String status = jsObj.getString("status");
        if (room == null && status.equals(RoomStatus.CREATED.toString())) {
            ArrayList<UserCardInfo> players = new ArrayList<UserCardInfo> ();
            players.add( new UserCardInfo(UserService.getUsername(), UserService.getAvatar()));
            ArrayList<String> invites = new ArrayList<String> ();
            JSONArray invitesJSArray = jsObj.getJSONArray("invites");
            for (int i = 0; i < invitesJSArray.length(); ++i) {
                invites.add(invitesJSArray.getString(i));
            }
            room = new Room(jsObj.getString("leader"), jsObj.getString("leader"), 
                invites, players, status);
            
            ws.SubscribeSalaAct();
        }
    }

    public static void updatePlayers (ArrayList<String> updated_players) {
        if (room != null) {
            ArrayList<UserCardInfo> updatedPlayers = new ArrayList<UserCardInfo> ();
            ArrayList<UserCardInfo> oldPlayers = room.getPlayers();
            Boolean encontrado = false;
            
            // Comprobar que si los jugadores existian ya.
            for (String player : updated_players) {
                for (int i = 0; i < oldPlayers.size(); ++i) {
                    if (oldPlayers.get(i).getUsername().equals(player)) {
                        encontrado = true;
                        updatedPlayers.add(oldPlayers.get(i));
                    }
                }
                if (!encontrado) {
                    //El jugador no estaba en la sala, se añade y 
                    //se busca su avatar
                    String username = player;
                    UserService userService = new UserService ();
                    JSONObject jsUserInfo = UserService.getUserInfo(username);
                    String avatar = jsUserInfo.getString("avatar");

                    updatedPlayers.add(new UserCardInfo(username, avatar));
                }
            }

            room.setPlayers(updatedPlayers);    
        }
    }

    public static void procesarMensajeAccionSala (Object payload) {
        if (room != null) {
            JSONObject jsObj = new JSONObject(payload.toString());
            String status = jsObj.getString("status");
            switch (status) {
                case "CREATED":
                case "CLOSE":
                case "SEARCHING":
                case "UPDATED_INVITES":
                case "UPDATED_PLAYERS":
                case "FOUND":
                case "FAILED":
                    room = null;
                    buscandoPartida = false;
                    crearSala();
                case "CANCELLED":
                    room.setStatus(RoomStatus.CREATED.toString());
                    buscandoPartida = false;
                default:
            }
        }
    }

    public static void procesarMensajeInvitacion (Object payload) {
        Invite invitacion;
        JSONObject jsObj = new JSONObject(payload.toString());
        String status = jsObj.getString("status");

        switch (status) {
            case "INVITED":
                invitacion = new Invite(jsObj.getString("leader"), jsObj.getString("id"));
                invites.add(invitacion);
                break;
            case "ACCEPTED":
                uniendoseASala = false;

                ArrayList<UserCardInfo> players = new ArrayList<UserCardInfo> ();
                players.add( new UserCardInfo(UserService.getUsername(), UserService.getAvatar()));
                ArrayList<String> invitesPL = new ArrayList<String> ();
                JSONArray invitesJSArray = jsObj.getJSONArray("invites");
                for (int i = 0; i < invitesJSArray.length(); ++i) {
                    invitesPL.add(invitesJSArray.getString(i));
                }
                room.setStatus("CREATED");
                room.setId(jsObj.getString("room"));
                room.setLeader(jsObj.getString("leader"));
                room.setPlayers(players);
                room.setInvites(invitesPL);

                ArrayList<String> updated_players = new ArrayList<String> ();
                JSONArray playersJSArray = jsObj.getJSONArray("players");
                for (int i = 0; i < playersJSArray.length(); ++i) {
                    updated_players.add(playersJSArray.getString(i));
                }
                RoomServices.updatePlayers(updated_players);

                ws.SubscribeSalaAct();

                break;
            case "OPEN":
                uniendoseASala = false;
                errorAlUnirseASala = true;
                crearSala();
                break;
            case "CANCELLED":
                invitacion = new Invite(jsObj.getString("leader"), jsObj.getString("id"));
                if (invites.contains(invitacion)) {
                    invites.remove(invitacion);
                }
                break;
            case "CLOSED":
                uniendoseASala = false;
                errorAlUnirseASala = true;
                crearSala();
                break;
            case "FULL":
                uniendoseASala = false;
                errorAlUnirseASala = true;
                crearSala();
                break;
            case "QUEUING":
                uniendoseASala = false;
                errorAlUnirseASala = true;
                crearSala();
                break;
            default:
        }

    }

    public static void crearSala () {
        if (room == null && !uniendoseASala && !creandoSala) {
            JSONObject myObject = new JSONObject();
            myObject.put("leader", UserService.getUsername());
            ws.session.send(ws.crearSala, myObject.toString());
            creandoSala = true;
        }
    }

    public static void cerrarSala (Boolean crearSala) {

    }

    public static void abandonarSala () {}

    public static void enviarInvitacion () {}

    public static void cancelarInvitacion (String leader, String roomId) {}

    public static void aceptarInvitacion (String leader, String roomId) {}

    //public static Boolean buscarPartida () {}

    //public static Boolean cancelarBusqueda () {}
}
