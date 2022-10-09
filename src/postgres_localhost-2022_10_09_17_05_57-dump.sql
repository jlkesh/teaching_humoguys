--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5 (Ubuntu 14.5-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.5 (Ubuntu 14.5-0ubuntu0.22.04.1)

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
-- Name: crypto; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA crypto;


ALTER SCHEMA crypto OWNER TO postgres;

--
-- Name: todo; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA todo;


ALTER SCHEMA todo OWNER TO postgres;

--
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA crypto;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- Name: isactive(bigint); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.isactive(IN userid bigint)
    LANGUAGE plpgsql
    AS $$
declare
    t_session_users record;
begin
    call isactive(userid, t_session_users);
end
$$;


ALTER PROCEDURE public.isactive(IN userid bigint) OWNER TO postgres;

--
-- Name: isactive(bigint, record); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.isactive(IN userid bigint, INOUT sessionuser record)
    LANGUAGE plpgsql
    AS $$
declare
    t_session_user record;
begin
    select * into t_session_user from todo.users t where t.id = userid;
    if not FOUND then
        raise exception 'In active user';
    end if;
    sessionuser = t_session_user;
end
$$;


ALTER PROCEDURE public.isactive(IN userid bigint, INOUT sessionuser record) OWNER TO postgres;

--
-- Name: password_encode(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.password_encode(raw_password character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS $$
begin
    return crypto.crypt(raw_password, crypto.gen_salt('bf', 8));
end
$$;


ALTER FUNCTION public.password_encode(raw_password character varying) OWNER TO postgres;

--
-- Name: password_match(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.password_match(raw_password character varying, encoded_password character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
begin
    return encoded_password = crypto.crypt(raw_password, encoded_password);
end
$$;


ALTER FUNCTION public.password_match(raw_password character varying, encoded_password character varying) OWNER TO postgres;

--
-- Name: password_strong(character varying); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.password_strong(IN password character varying)
    LANGUAGE plpgsql
    AS $_$
BEGIN
    if not (password ~ '^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$') then
        raise exception 'Password must be more than or equal 8 characters';
    end if;
end
$_$;


ALTER PROCEDURE public.password_strong(IN password character varying) OWNER TO postgres;

--
-- Name: todo_create(character varying, bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.todo_create(title character varying, userid bigint) RETURNS text
    LANGUAGE plpgsql
    AS $$

declare
    r_todo record;
BEGIN

    call isactive(userid);
    if title is null then
        raise exception 'Title can not be null';
    end if;
    insert into todo.todos (title, user_id)
    values (title, userid)
    returning * into r_todo;
    return row_to_json(r_todo)::text;

END
$$;


ALTER FUNCTION public.todo_create(title character varying, userid bigint) OWNER TO postgres;

--
-- Name: todo_delete(bigint, bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.todo_delete(todoid bigint, userid bigint) RETURNS text
    LANGUAGE plpgsql
    AS $$

declare
    r_todo record;
BEGIN

    call isactive(userid);

    select * into r_todo from todo.todos t where t.id = todoid;

    if not FOUND then
        raise exception 'Todo not found';
    end if;

    if r_todo.user_id <> userid then
        raise exception 'Permission denied';
    end if;

    delete from todo.todos where id = todoid;
    return 'successfully deleted';
END
$$;


ALTER FUNCTION public.todo_delete(todoid bigint, userid bigint) OWNER TO postgres;

--
-- Name: todo_update(character varying, bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.todo_update(dataparam character varying, userid bigint) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
declare
    dataJson   json;
    t_todo     record;
    v_title    varchar;
    v_done     bool;
    v_id       bigint;
begin
    call isactive(userid);
    if dataparam is null or dataparam = '{}'::text then
        raise exception 'Dataparam can not be null';
    end if;

    dataJson := dataparam::json;

    v_id := (dataJson ->> 'todo_id')::bigint;

    select * into t_todo from todo.todos where id = v_id;

    if not FOUND then
        raise exception 'Todo not found';
    end if;

    if t_todo.user_id <> userid then
        raise exception 'Permission denied';
    end if;

    if dataJson ->> 'title' is null then
        v_title := t_todo.title;
    else
        v_title := dataJson ->> 'title';
    end if;

    if dataJson ->> 'done' is null then
        v_done := t_todo.done;
    else
        v_done := dataJson ->> 'done';
    end if;



    update todo.todos
    set title    = v_title,
        done= v_done
    where id = v_id;

    return v_id;
end
$$;


ALTER FUNCTION public.todo_update(dataparam character varying, userid bigint) OWNER TO postgres;

--
-- Name: user_delete(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.user_delete() RETURNS text
    LANGUAGE plpgsql
    AS $$
declare
    t_users     record;
    jsonBuilder jsonb;
BEGIN

    select * into t_users from users t where t.username = in_username;

    if not FOUND then
        raise exception 'User("%") not found',in_username;
    end if;

    if not todo.match_password(in_password, t_users.password) then
        raise exception 'Bad credentials';
    end if;

END
$$;


ALTER FUNCTION public.user_delete() OWNER TO postgres;

--
-- Name: user_info(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.user_info(userid bigint) RETURNS text
    LANGUAGE plpgsql
    AS $$
declare
    jsonBuilder jsonb;
    t_users     record;
begin
    call isactive(userid, t_users);
    jsonBuilder = jsonb_build_object('id', t_users.id);
    jsonBuilder = jsonBuilder || jsonb_build_object('username', t_users.username);
    jsonBuilder = jsonBuilder || jsonb_build_object('role', t_users.role);
    jsonBuilder = jsonBuilder || jsonb_build_object('uuid', t_users.uuid);
    jsonBuilder = jsonBuilder || jsonb_build_object('created_at', t_users.created_at);
    jsonBuilder = jsonBuilder || jsonb_build_object('last_login_at', t_users.last_login_at);
    return jsonBuilder::text;
end
$$;


ALTER FUNCTION public.user_info(userid bigint) OWNER TO postgres;

--
-- Name: user_login(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.user_login(in_username character varying, in_password character varying) RETURNS text
    LANGUAGE plpgsql
    AS $$
declare
    t_users todo.users%rowtype;
BEGIN
    select * into t_users from todo.users t where t.username = in_username;
    if not FOUND then
        raise exception 'User("%") not found',in_username;
    end if;
    if not password_match(in_password, t_users.password) then
        raise exception 'Bad credentials';
    end if;
    return user_info(t_users.id);
END
$$;


ALTER FUNCTION public.user_login(in_username character varying, in_password character varying) OWNER TO postgres;

--
-- Name: user_register(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.user_register(dataparam text DEFAULT NULL::text) RETURNS bigint
    LANGUAGE plpgsql
    AS $$

declare
    dataJson   json;
    v_role     varchar := 'USER';
    v_username varchar;
    v_password varchar;
    newID      bigint;
    t_users    todo.users%rowtype;
begin
    if dataparam is null or dataparam = '{}'::text then
        raise exception 'dataparam can not be null or empty';
    end if;

    dataJson := dataparam::json;

/*
    if exists(select * from users where username = dataJson ->> 'username') then
        raise exception 'Username already taken';
    end if;
*/

    v_username := dataJson ->> 'username';
    v_password := dataJson ->> 'password';

    if v_username is null then
        raise exception 'Username can not be null';
    end if;

    if v_password is null then
        raise exception 'Password can not be null';
    end if;

    call password_strong(v_password);

    select * into t_users from todo.users where username = v_username;

    if FOUND then
        raise exception 'Username ("%") already taken',v_username;
    end if;

    insert into todo.users (username, password, role)
    values (v_username,
            password_encode(v_password),
            v_role)
    returning id into newID;

    return newID;
end
$$;


ALTER FUNCTION public.user_register(dataparam text) OWNER TO postgres;

--
-- Name: user_todos(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.user_todos(userid bigint) RETURNS text
    LANGUAGE plpgsql
    AS $$
declare
    t_user record;
begin
    call isactive(userid, t_user);
    return  array_to_json((select array_agg(row)
                                              from (select * from todo.todos t where t.user_id = userid) row))::text;
end
$$;


ALTER FUNCTION public.user_todos(userid bigint) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: price; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.price (
    book_id integer NOT NULL,
    name character varying(50) NOT NULL,
    author character varying(50) NOT NULL,
    sold character varying(255) NOT NULL
);


ALTER TABLE public.price OWNER TO postgres;

--
-- Name: price_book_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.price_book_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.price_book_id_seq OWNER TO postgres;

--
-- Name: price_book_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.price_book_id_seq OWNED BY public.price.book_id;


--
-- Name: student; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student (
    id bigint NOT NULL,
    full_name character varying,
    age smallint DEFAULT 12,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.student OWNER TO postgres;

--
-- Name: student_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.student_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.student_id_seq OWNER TO postgres;

--
-- Name: student_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.student_id_seq OWNED BY public.student.id;


--
-- Name: todos; Type: TABLE; Schema: todo; Owner: postgres
--

CREATE TABLE todo.todos (
    id bigint NOT NULL,
    title character varying NOT NULL,
    done boolean DEFAULT false NOT NULL,
    user_id bigint,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    uuid uuid DEFAULT gen_random_uuid()
);


ALTER TABLE todo.todos OWNER TO postgres;

--
-- Name: todos_id_seq; Type: SEQUENCE; Schema: todo; Owner: postgres
--

CREATE SEQUENCE todo.todos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE todo.todos_id_seq OWNER TO postgres;

--
-- Name: todos_id_seq; Type: SEQUENCE OWNED BY; Schema: todo; Owner: postgres
--

ALTER SEQUENCE todo.todos_id_seq OWNED BY todo.todos.id;


--
-- Name: users; Type: TABLE; Schema: todo; Owner: postgres
--

CREATE TABLE todo.users (
    id bigint NOT NULL,
    username character varying NOT NULL,
    password character varying NOT NULL,
    uuid character varying(36) DEFAULT gen_random_uuid() NOT NULL,
    role character varying NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_login_at timestamp with time zone
);


ALTER TABLE todo.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: todo; Owner: postgres
--

CREATE SEQUENCE todo.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE todo.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: todo; Owner: postgres
--

ALTER SEQUENCE todo.users_id_seq OWNED BY todo.users.id;


--
-- Name: price book_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.price ALTER COLUMN book_id SET DEFAULT nextval('public.price_book_id_seq'::regclass);


--
-- Name: student id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student ALTER COLUMN id SET DEFAULT nextval('public.student_id_seq'::regclass);


--
-- Name: todos id; Type: DEFAULT; Schema: todo; Owner: postgres
--

ALTER TABLE ONLY todo.todos ALTER COLUMN id SET DEFAULT nextval('todo.todos_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: todo; Owner: postgres
--

ALTER TABLE ONLY todo.users ALTER COLUMN id SET DEFAULT nextval('todo.users_id_seq'::regclass);


--
-- Data for Name: price; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.price (book_id, name, author, sold) FROM stdin;
\.


--
-- Data for Name: student; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.student (id, full_name, age, created_at) FROM stdin;
2	Omonov Bunyod	12	2022-08-14 11:07:35.355741
3	Hasanov NurIslom	12	2022-08-14 11:07:35.355741
4	Mingyasharov Akmal	12	2022-08-14 11:07:46.183759
6	Hasanov NurIslom	12	2022-08-14 11:07:46.183759
9	Choyuz	16	2022-08-14 11:11:41.891752
1	Shohista	12	2022-08-14 11:07:35.355741
\.


--
-- Data for Name: todos; Type: TABLE DATA; Schema: todo; Owner: postgres
--

COPY todo.todos (id, title, done, user_id, created_at, uuid) FROM stdin;
7	Stay Hungry Stay Foolish	t	1	2022-10-09 17:00:08.667486	97c44e63-3380-4027-8c65-3e4ce61ce7b8
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: todo; Owner: postgres
--

COPY todo.users (id, username, password, uuid, role, created_at, last_login_at) FROM stdin;
2	john	$2a$08$EomuLaSnRcqsIyG6zE3cu..UzLE8slC/hKQ/4HtW3tzmzU.j6VE3C	5b73bd79-8235-4183-ac49-6080a1a8a17c	USER	2022-10-02 09:53:59.940587	2022-10-02 10:41:09.474741+05
1	jl	$2a$08$EomuLaSnRcqsIyG6zE3cu..UzLE8slC/hKQ/4HtW3tzmzU.j6VE3C	79c78123-a3a1-4b52-9abb-da65e7d71c9f	USER	2022-10-02 04:04:39.022998	2022-10-09 13:58:11.859183+05
\.


--
-- Name: price_book_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.price_book_id_seq', 1, false);


--
-- Name: student_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.student_id_seq', 9, true);


--
-- Name: todos_id_seq; Type: SEQUENCE SET; Schema: todo; Owner: postgres
--

SELECT pg_catalog.setval('todo.todos_id_seq', 7, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: todo; Owner: postgres
--

SELECT pg_catalog.setval('todo.users_id_seq', 2, true);


--
-- Name: price price_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.price
    ADD CONSTRAINT price_name_key UNIQUE (name);


--
-- Name: price price_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.price
    ADD CONSTRAINT price_pkey PRIMARY KEY (book_id);


--
-- Name: price price_sold_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.price
    ADD CONSTRAINT price_sold_key UNIQUE (sold);


--
-- Name: student student_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_pkey PRIMARY KEY (id);


--
-- Name: todos todos_pkey; Type: CONSTRAINT; Schema: todo; Owner: postgres
--

ALTER TABLE ONLY todo.todos
    ADD CONSTRAINT todos_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: todo; Owner: postgres
--

ALTER TABLE ONLY todo.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: todo; Owner: postgres
--

ALTER TABLE ONLY todo.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: todos todos_user_id_fkey; Type: FK CONSTRAINT; Schema: todo; Owner: postgres
--

ALTER TABLE ONLY todo.todos
    ADD CONSTRAINT todos_user_id_fkey FOREIGN KEY (user_id) REFERENCES todo.users(id);


--
-- PostgreSQL database dump complete
--

