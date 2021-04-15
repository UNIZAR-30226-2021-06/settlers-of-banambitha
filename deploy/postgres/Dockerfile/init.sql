--
-- PostgreSQL database cluster dump
--

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Roles
--

ALTER ROLE postgres WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION BYPASSRLS PASSWORD 'md543cdf645de54bfe96d9b1b7256463a28';






--
-- Databases
--

--
-- Database "template1" dump
--

\connect template1

--
-- PostgreSQL database dump
--

-- Dumped from database version 13.2 (Debian 13.2-1.pgdg100+1)
-- Dumped by pg_dump version 13.2 (Debian 13.2-1.pgdg100+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- PostgreSQL database dump complete
--

--
-- Database "catan_db" dump
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 13.2 (Debian 13.2-1.pgdg100+1)
-- Dumped by pg_dump version 13.2 (Debian 13.2-1.pgdg100+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: catan_db; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE catan_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'en_US.utf8';


ALTER DATABASE catan_db OWNER TO postgres;

\connect catan_db

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: amigo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.amigo (
    usuario1_id character varying(255) NOT NULL,
    usuario2_id character varying(255) NOT NULL
);


ALTER TABLE public.amigo OWNER TO postgres;

--
-- Name: dispone; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dispone (
    producto_id character varying(255) NOT NULL,
    usuario_id character varying(255) NOT NULL
);


ALTER TABLE public.dispone OWNER TO postgres;

--
-- Name: estadisticas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.estadisticas (
    usuario_id character varying(255) NOT NULL,
    mayor_racha_de_victorias integer DEFAULT 0 NOT NULL,
    partidas_jugadas integer DEFAULT 0 NOT NULL,
    porcentaje_de_victorias real DEFAULT 0 NOT NULL,
    racha_de_victorias_actual integer DEFAULT 0 NOT NULL,
    total_de_victorias integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.estadisticas OWNER TO postgres;

--
-- Name: peticion_amistad; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.peticion_amistad (
    usuario1_id character varying(255) NOT NULL,
    usuario2_id character varying(255) NOT NULL
);


ALTER TABLE public.peticion_amistad OWNER TO postgres;

--
-- Name: producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto (
    producto_id character varying(255) NOT NULL,
    precio integer NOT NULL,
    tipo character varying(10) NOT NULL,
    CONSTRAINT producto_tipo_check CHECK (((tipo)::text = ANY ((ARRAY['AVATAR'::character varying, 'APARIENCIA'::character varying])::text[])))
);


ALTER TABLE public.producto OWNER TO postgres;

--
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    usuario_id character varying(255) NOT NULL,
    apariencia character varying(100) DEFAULT 'Clasica'::character varying NOT NULL,
    avatar character varying(100) DEFAULT 'Original'::character varying NOT NULL,
    contrasenya character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    idioma character varying(10) DEFAULT 'Español'::character varying NOT NULL,
    saldo integer DEFAULT 0 NOT NULL,
    CONSTRAINT usuario_idioma_check CHECK (((idioma)::text = ANY ((ARRAY['Español'::character varying, 'English'::character varying])::text[]))),
    CONSTRAINT usuario_saldo_check CHECK ((saldo >= 0))
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- Data for Name: amigo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.amigo (usuario1_id, usuario2_id) FROM stdin;
\.


--
-- Data for Name: dispone; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dispone (producto_id, usuario_id) FROM stdin;
\.


--
-- Data for Name: estadisticas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.estadisticas (usuario_id, mayor_racha_de_victorias, partidas_jugadas, porcentaje_de_victorias, racha_de_victorias_actual, total_de_victorias) FROM stdin;
\.


--
-- Data for Name: peticion_amistad; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.peticion_amistad (usuario1_id, usuario2_id) FROM stdin;
\.


--
-- Data for Name: producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.producto (producto_id, precio, tipo) FROM stdin;
Clasica	10	APARIENCIA
Original	10	AVATAR
\.


--
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuario (usuario_id, apariencia, avatar, contrasenya, email, idioma, saldo) FROM stdin;
\.


--
-- Name: amigo amigo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.amigo
    ADD CONSTRAINT amigo_pkey PRIMARY KEY (usuario1_id, usuario2_id);


--
-- Name: dispone dispone_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dispone
    ADD CONSTRAINT dispone_pkey PRIMARY KEY (producto_id, usuario_id);


--
-- Name: estadisticas estadisticas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.estadisticas
    ADD CONSTRAINT estadisticas_pkey PRIMARY KEY (usuario_id);


--
-- Name: peticion_amistad peticion_amistad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.peticion_amistad
    ADD CONSTRAINT peticion_amistad_pkey PRIMARY KEY (usuario1_id, usuario2_id);


--
-- Name: producto producto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_pkey PRIMARY KEY (producto_id);


--
-- Name: usuario uk_5171l57faosmj8myawaucatdw; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT uk_5171l57faosmj8myawaucatdw UNIQUE (email);


--
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (usuario_id);


--
-- Name: peticion_amistad fk7l2nsjseysvdo4pyxwhvcciux; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.peticion_amistad
    ADD CONSTRAINT fk7l2nsjseysvdo4pyxwhvcciux FOREIGN KEY (usuario2_id) REFERENCES public.usuario(usuario_id);


--
-- Name: dispone fkaeqrplae579l1meqfk9ac8as9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dispone
    ADD CONSTRAINT fkaeqrplae579l1meqfk9ac8as9 FOREIGN KEY (usuario_id) REFERENCES public.usuario(usuario_id);


--
-- Name: usuario fkdfk99t0hfmovvhgstxveqkvfp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT fkdfk99t0hfmovvhgstxveqkvfp FOREIGN KEY (apariencia) REFERENCES public.producto(producto_id);


--
-- Name: peticion_amistad fkjae9pjy0u50bfxg7mhoi2f3w4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.peticion_amistad
    ADD CONSTRAINT fkjae9pjy0u50bfxg7mhoi2f3w4 FOREIGN KEY (usuario1_id) REFERENCES public.usuario(usuario_id);


--
-- Name: usuario fkl10i1uyn56clheewiab625w9i; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT fkl10i1uyn56clheewiab625w9i FOREIGN KEY (avatar) REFERENCES public.producto(producto_id);


--
-- Name: estadisticas fkm6s1kx73hsaqrcmau7exol7lw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.estadisticas
    ADD CONSTRAINT fkm6s1kx73hsaqrcmau7exol7lw FOREIGN KEY (usuario_id) REFERENCES public.usuario(usuario_id);


--
-- Name: amigo fkt0aldcfr5umtqnp7veeg75m0l; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.amigo
    ADD CONSTRAINT fkt0aldcfr5umtqnp7veeg75m0l FOREIGN KEY (usuario1_id) REFERENCES public.usuario(usuario_id);


--
-- Name: dispone fktfckcl3su65xh6l681rm7m82h; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dispone
    ADD CONSTRAINT fktfckcl3su65xh6l681rm7m82h FOREIGN KEY (producto_id) REFERENCES public.producto(producto_id);


--
-- Name: amigo fkwyeatyjd7b03k5q2pkcr4ust; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.amigo
    ADD CONSTRAINT fkwyeatyjd7b03k5q2pkcr4ust FOREIGN KEY (usuario2_id) REFERENCES public.usuario(usuario_id);


--
-- PostgreSQL database dump complete
--

--
-- Database "postgres" dump
--

\connect postgres

--
-- PostgreSQL database dump
--

-- Dumped from database version 13.2 (Debian 13.2-1.pgdg100+1)
-- Dumped by pg_dump version 13.2 (Debian 13.2-1.pgdg100+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- PostgreSQL database dump complete
--

--
-- PostgreSQL database cluster dump complete
--

