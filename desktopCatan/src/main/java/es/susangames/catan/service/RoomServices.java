package es.susangames.catan.service;

import java.util.ArrayList;
import org.json.*;

import es.susangames.catan.service.ws;

public class RoomServices {
    
    enum RoomMsgStatus {
        FOUND, SEARCHING, FAILED, UPDATED_PLAYERS, UPDATED_INVITES,
        CLOSED, CREATED, CANCELLED
    }

    enum InviteMsgstatus {
        INVITED, ACCEPTED, CANCELLED, FULL, QUEUING, CLOSED, OPEN
    }

    enum RoomStatus {
        SEARCHING, CREATED
    }

    private static class UserCardInfo {
        String _username;
        String _avatar;

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

    private static class Room {
        String _leader;
        String _id;
        String _invites;
        String _players;
        String _status;

        Room (String leader, String id, String invites, String players, String status) {
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

        void setInvites (String invites) {
            this._invites = invites;
        }

        String getInvites () {
            return this._invites;
        }

        void setPlayers (String players) {
            this._players = players;
        }

        String getPlayers () {
            return this._players;
        }

        void setStatus (String status) {
            this._status = status;
        }

        String getStatus () {
            return this._status;
        }
    }

    private static class Invite {
        String _leader;
        String _id;

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
    private static Room room = null;
    private static ArrayList<Invite> invites = new ArrayList<Invite> ();
    private static Boolean buscandoPartida = false;
    public static Boolean uniendoseASala = false;
    public static Boolean creandoSala  = false;
    public static Boolean liderCerroSala = false;
    public static Boolean errorAlUnirseASala = false;

    public RoomServices () {
        
    }

    //public static Boolean soyLider () {}

    //public static Boolean busquedaIniciada () {}

    public static void procesarMensajeCreacionSala (Object payload) {
        System.out.println(payload.toString());
    }

    public static void updatePlayers (ArrayList<String> updated_players) {}

    public static void procesarMensajeAccionSala (Object msg) {}

    public static void procesarMensajeInvitacion (Object msg) {

    }

    public static void crearSala () {
        if (room == null && !uniendoseASala && !creandoSala) {
            JSONObject myObject = new JSONObject();
            myObject.put("leader", UserService.getUsername());
            ws.session.send(ws.crearSala, myObject.toString());
            creandoSala = true;
        }
    }

    public static void cerrarSala (Boolean crearSala) {}

    public static void abandonarSala () {}

    public static void enviarInvitacion () {}

    public static void cancelarInvitacion (String leader, String roomId) {}

    public static void aceptarInvitacion (String leader, String roomId) {}

    //public static Boolean buscarPartida () {}

    //public static Boolean cancelarBusqueda () {}
}
