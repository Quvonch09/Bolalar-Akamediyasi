--
-- PostgreSQL database dump
--

\restrict dhm57cTEN8cch2dt3CZW4dXPuRsWB1DzCy5ictP0EosHtwe01OzBUzsrd9lPG3c

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
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
-- Name: attendance; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.attendance (
    active boolean NOT NULL,
    date date,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    sinf_id uuid,
    student_id uuid,
    created_by character varying(255),
    description character varying(255),
    status character varying(255),
    updated_by character varying(255),
    CONSTRAINT attendance_status_check CHECK (((status)::text = ANY ((ARRAY['KELDI'::character varying, 'KELMADI'::character varying, 'SABABLI'::character varying])::text[])))
);


ALTER TABLE public.attendance OWNER TO postgres;

--
-- Name: block_progress; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.block_progress (
    active boolean NOT NULL,
    answered boolean,
    correct boolean,
    answered_at timestamp(6) with time zone,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    block_id uuid,
    id uuid NOT NULL,
    student_id uuid,
    answer character varying(255),
    created_by character varying(255),
    updated_by character varying(255)
);


ALTER TABLE public.block_progress OWNER TO postgres;

--
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book (
    active boolean NOT NULL,
    grade_level integer,
    total_pages integer,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    lesson_id uuid,
    subject_id uuid,
    cover_image_url character varying(255),
    created_by character varying(255),
    description text,
    file_url character varying(255) NOT NULL,
    original_file_name character varying(255) NOT NULL,
    status character varying(255),
    title character varying(255) NOT NULL,
    updated_by character varying(255),
    CONSTRAINT book_status_check CHECK (((status)::text = ANY ((ARRAY['PROCESSING'::character varying, 'READY'::character varying, 'FAILED'::character varying])::text[])))
);


ALTER TABLE public.book OWNER TO postgres;

--
-- Name: book_page; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_page (
    height integer,
    page_number integer,
    width integer,
    book_id uuid,
    id uuid NOT NULL,
    image_url character varying(255)
);


ALTER TABLE public.book_page OWNER TO postgres;

--
-- Name: book_progress; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_progress (
    active boolean NOT NULL,
    completed boolean,
    current_page integer,
    progress_percent double precision,
    total_pages integer,
    created_at timestamp(6) with time zone NOT NULL,
    last_read_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    book_id uuid,
    id uuid NOT NULL,
    student_id uuid,
    created_by character varying(255),
    updated_by character varying(255)
);


ALTER TABLE public.book_progress OWNER TO postgres;

--
-- Name: class; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.class (
    active boolean NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    teacher_id uuid,
    created_by character varying(255),
    name character varying(255) NOT NULL,
    shift_enum character varying(255),
    updated_by character varying(255),
    CONSTRAINT class_shift_enum_check CHECK (((shift_enum)::text = ANY ((ARRAY['SMENA_1'::character varying, 'SMENA_2'::character varying])::text[])))
);


ALTER TABLE public.class OWNER TO postgres;

--
-- Name: coin; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.coin (
    id uuid NOT NULL,
    active boolean NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by character varying(255),
    updated_at timestamp(6) with time zone,
    updated_by character varying(255),
    coin integer NOT NULL,
    feedback character varying(255),
    student_id uuid
);


ALTER TABLE public.coin OWNER TO postgres;

--
-- Name: feedback; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.feedback (
    active boolean NOT NULL,
    rating integer NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    teacher_id uuid,
    created_by character varying(255),
    title character varying(255),
    updated_by character varying(255),
    CONSTRAINT feedback_rating_check CHECK ((rating <= 5))
);


ALTER TABLE public.feedback OWNER TO postgres;

--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO postgres;

--
-- Name: homework; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.homework (
    active boolean NOT NULL,
    deadline integer,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    lesson_id uuid,
    created_by character varying(255),
    deadline_enum character varying(255),
    description character varying(255),
    title character varying(255),
    updated_by character varying(255),
    CONSTRAINT homework_deadline_enum_check CHECK (((deadline_enum)::text = ANY ((ARRAY['HOUR'::character varying, 'DAY'::character varying, 'WEEK'::character varying, 'MONTH'::character varying])::text[])))
);


ALTER TABLE public.homework OWNER TO postgres;

--
-- Name: lesson; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lesson (
    active boolean NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    subject_id uuid,
    created_by character varying(255),
    description character varying(255),
    file_url character varying(255),
    name character varying(255),
    updated_by character varying(255)
);


ALTER TABLE public.lesson OWNER TO postgres;

--
-- Name: mark; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mark (
    active boolean NOT NULL,
    active_score integer,
    behaviour_score integer,
    date date,
    homework_score integer,
    total_score integer,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    student_id uuid,
    created_by character varying(255),
    mark_category_status character varying(255),
    updated_by character varying(255),
    CONSTRAINT mark_active_score_check CHECK (((active_score >= 1) AND (active_score <= 10))),
    CONSTRAINT mark_behaviour_score_check CHECK (((behaviour_score >= 1) AND (behaviour_score <= 10))),
    CONSTRAINT mark_homework_score_check CHECK (((homework_score >= 1) AND (homework_score <= 10))),
    CONSTRAINT mark_mark_category_status_check CHECK (((mark_category_status)::text = ANY ((ARRAY['YASHIL'::character varying, 'QIZIL'::character varying, 'SARIQ'::character varying])::text[])))
);


ALTER TABLE public.mark OWNER TO postgres;

--
-- Name: notification; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notification (
    active boolean NOT NULL,
    is_read boolean NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    parent_id uuid,
    student_id uuid,
    created_by character varying(255),
    description character varying(255),
    message character varying(255),
    updated_by character varying(255)
);


ALTER TABLE public.notification OWNER TO postgres;

--
-- Name: page_block; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.page_block (
    active boolean NOT NULL,
    completed boolean,
    height double precision,
    required boolean,
    width double precision,
    x double precision,
    y double precision,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    page_id uuid,
    body text,
    created_by character varying(255),
    image_url character varying(255),
    title character varying(255),
    type character varying(255),
    updated_by character varying(255),
    CONSTRAINT page_block_type_check CHECK (((type)::text = ANY ((ARRAY['INFO'::character varying, 'QUESTION'::character varying, 'QUIZ'::character varying, 'IMAGE'::character varying])::text[])))
);


ALTER TABLE public.page_block OWNER TO postgres;

--
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    id uuid NOT NULL,
    active boolean NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by character varying(255),
    updated_at timestamp(6) with time zone,
    updated_by character varying(255),
    count_coin integer NOT NULL,
    description character varying(500),
    img_url character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE public.product OWNER TO postgres;

--
-- Name: student; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student (
    active boolean NOT NULL,
    age integer,
    coin integer NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    parent_id uuid,
    sinf_id uuid,
    created_by character varying(255),
    first_name character varying(255) NOT NULL,
    img_url character varying(255),
    last_name character varying(255),
    password character varying(255) NOT NULL,
    phone character varying(255) NOT NULL,
    updated_by character varying(255)
);


ALTER TABLE public.student OWNER TO postgres;

--
-- Name: subject; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.subject (
    active boolean NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    created_by character varying(255),
    description character varying(255),
    name character varying(255),
    updated_by character varying(255)
);


ALTER TABLE public.subject OWNER TO postgres;

--
-- Name: submission; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.submission (
    active boolean NOT NULL,
    score integer NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    homework_id uuid,
    id uuid NOT NULL,
    student_id uuid,
    created_by character varying(255),
    feedback character varying(255),
    file_url character varying(255),
    text_answer character varying(255),
    updated_by character varying(255),
    CONSTRAINT submission_score_check CHECK (((score >= 1) AND (score <= 10)))
);


ALTER TABLE public.submission OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    active boolean NOT NULL,
    enabled boolean NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    created_by character varying(255),
    first_name character varying(255) NOT NULL,
    img_url character varying(255),
    last_name character varying(255),
    password character varying(255) NOT NULL,
    phone character varying(255) NOT NULL,
    role character varying(255),
    updated_by character varying(255),
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['ROLE_TEACHER'::character varying, 'ROLE_PARENT'::character varying, 'ROLE_ADMIN'::character varying, 'ROLE_SUPER_ADMIN'::character varying])::text[])))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: video_lesson; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.video_lesson (
    active boolean NOT NULL,
    duration integer,
    created_at timestamp(6) with time zone NOT NULL,
    file_size bigint,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    lesson_id uuid,
    created_by character varying(255),
    format character varying(255),
    title character varying(255),
    updated_by character varying(255),
    video_url character varying(255)
);


ALTER TABLE public.video_lesson OWNER TO postgres;

--
-- Name: video_progress; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.video_progress (
    active boolean NOT NULL,
    completed boolean,
    watched_seconds integer,
    created_at timestamp(6) with time zone NOT NULL,
    last_watched_at timestamp(6) without time zone,
    updated_at timestamp(6) with time zone,
    id uuid NOT NULL,
    student_id uuid,
    video_lesson_id uuid,
    created_by character varying(255),
    updated_by character varying(255),
    last_position_seconds integer
);


ALTER TABLE public.video_progress OWNER TO postgres;

--
-- Data for Name: attendance; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.attendance (active, date, created_at, updated_at, id, sinf_id, student_id, created_by, description, status, updated_by) FROM stdin;
\.


--
-- Data for Name: block_progress; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.block_progress (active, answered, correct, answered_at, created_at, updated_at, block_id, id, student_id, answer, created_by, updated_by) FROM stdin;
\.


--
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book (active, grade_level, total_pages, created_at, updated_at, id, lesson_id, subject_id, cover_image_url, created_by, description, file_url, original_file_name, status, title, updated_by) FROM stdin;
t	11	54	2026-05-04 14:33:49.801733+05	2026-05-04 14:33:49.801733+05	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	3fa85f64-5717-4562-b3fc-2c963f66afa7	3fa85f64-5717-4562-b3fc-2c963f66afa6	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/da499d26-ee61-4f53-aea0-5a2f14a2d576.png	998900000000	molxona haqida	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/files/4c66dd85-9136-4613-9c79-3779be41aa5a.pdf	Jorj Oruell. Hayvonlar xo'jaligi haqida g'aroyib qissa.pdf	READY	Molxona	998900000000
\.


--
-- Data for Name: book_page; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_page (height, page_number, width, book_id, id, image_url) FROM stdin;
1637	1	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	581c382e-58c3-46d1-b857-a2e2d1d9080a	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/da499d26-ee61-4f53-aea0-5a2f14a2d576.png
1637	2	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	3d2417c9-8236-4876-b6cc-f4da53f03883	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/26f54f6f-ef32-440f-9e8d-faaaedb79dff.png
1637	3	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	aebb8bab-6b40-4bc0-831e-a77a7d4e8181	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/6d6ef95b-e1cc-4a4f-9452-30bda840408a.png
1637	4	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	7862eecc-64ce-410c-93db-17702b53f34b	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/d8e53e14-747e-4fac-8052-85d274cd2df1.png
1637	5	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	f44b3bf2-2cba-4981-b8cb-2ba7d2661ef2	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/5c2fd100-eeb8-4690-9e41-cc3a1379d4c7.png
1637	6	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	873107d0-9264-4a28-a326-cca27f7810be	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/fb27e6c3-f69e-4d92-91e9-5b9ec216935d.png
1637	7	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	5c505e0b-3c02-45b9-9638-a600ef1b1bdf	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/0b8b639f-731e-43aa-b3de-06d66cf06ae8.png
1637	8	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	fb1fb57c-c6f2-472f-af0b-a3bed099b3e7	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/911160e7-9c6b-43e6-8bc1-5feca96c4178.png
1637	9	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	5597feec-5b5e-4f95-8d4c-8d6755dfe760	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/21b0c2b8-0503-4c3e-8291-bfa882be4921.png
1637	10	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	6e679fe2-e8b2-4fc3-ba91-6c1cea54699b	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/f0b88bac-c5b6-407c-a971-dcc5de3cccf8.png
1637	11	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	447a97d2-4c05-4577-aac2-f22277220336	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/a3451f77-a565-48c4-9f53-32f0d86f6f8d.png
1637	12	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	5ed919c8-c42c-46a9-8db1-ae091132ca25	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/59545661-ad97-4866-8cb1-790e1a71fcee.png
1637	13	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	0f1325cc-5977-4f93-ad4b-931b0fac9cf7	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/004bbec1-9e83-45cc-aaee-aaefedb60558.png
1637	14	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	d4f4bedd-6bb3-4171-b499-64ffc995d594	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/d909e99c-9a42-4557-b293-07fd0d16f907.png
1637	15	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	209636c0-de78-43c7-8cea-84a8ef7896a4	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/cd0216c8-7d79-41e4-aae0-2e8bd5019aef.png
1637	16	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	33b9aed9-1023-4747-b84c-ac2353995b3d	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/50e7a7d9-176f-4719-83ae-b7386bdb566b.png
1637	17	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	2b07443e-3ac0-42c8-8d1d-0cebc0d76815	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/8a764988-00b3-432e-97e0-321b2cd66f41.png
1637	18	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	276f0c29-50b3-4ae3-92ff-1f0d87f1df5c	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/8b0d1f34-94ff-47bb-9852-e79d353b7e96.png
1637	19	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	216a9015-2abe-45b3-9f35-e80d7cccf45a	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/abb723d7-c23a-4634-83ee-4027f3680dcc.png
1637	20	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	e536e1db-eeae-4d0d-bdfb-38a20b09aea2	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/27187c65-57aa-4ef5-8381-13603fb05a98.png
1637	21	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	fe7b2652-e045-4220-bdfc-ada4861a79e5	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/d2c9214d-e3e5-4fed-b642-6965d9100ca6.png
1637	22	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	8c53fb3f-9ee2-4834-9e40-e895783a2c94	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/54800bfe-5b55-4e9a-9a1a-d71d36480ba7.png
1637	23	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	35b6bd1f-309f-41f1-8734-a023b6a1d90e	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/2b4e7f1e-698f-40a5-b3ed-e0a190e56d38.png
1637	24	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	6ca4f08d-2483-46f9-b8bc-912264322c90	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/adc562b7-e19d-4f43-b4b5-d4653935bd30.png
1637	25	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	abdffed3-3aba-4415-b592-c9d813c729c7	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/7c265135-cf33-4540-81c5-d7d44e70e6d5.png
1637	26	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	c3eefb01-fc51-4ffc-841e-6f451467fe41	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/72ce72da-a6fb-403d-986a-81e42c26d638.png
1637	27	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	6fe1b500-5194-4afa-a957-11125f8d5ff0	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/cec0a458-ed36-43f7-993f-c2b694c728e4.png
1637	28	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	086306c4-90b9-4b3a-9726-d3d1b680ee1d	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/6c08be22-3cea-4831-91d7-1494d74bce23.png
1637	29	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	60caeebe-a7d6-437b-bccb-27d5400101b4	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/109116ef-48f4-4c57-b184-262e0a3a024c.png
1637	30	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	bcfa30a5-1df2-49a2-8c45-2defe6747994	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/f32eaf28-79d2-439f-aba1-acbf3ccd2b5f.png
1637	31	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	cd6d5865-0c95-40df-8deb-59acc501918f	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/943a1d85-a8b2-4ce0-a705-5183d4bd413d.png
1637	32	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	739373f0-3502-4334-bc78-44dd9be859c4	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/2b159bcc-cc74-4f2a-9a69-29b60074b35b.png
1637	33	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	bc195cda-ce37-412c-888e-19bf96a2d379	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/d0067579-d8da-43f0-bc03-8c450751dda6.png
1637	34	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	878c7df2-d227-4029-96f4-c1964e5756fb	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/77013a03-48a1-43f5-bbc9-0e08242933f6.png
1637	35	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	c9a15392-d07e-4625-8c08-4cb407577d5f	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/49c07601-00e9-4ea4-98e5-a7b0b7afbb3a.png
1637	36	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	28ffa854-b96f-4292-9ec2-64d1fe9e1b29	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/97667674-8b2c-4168-929a-3f34b1f900c6.png
1637	37	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	cdf76b6d-2555-410f-bfa4-e6b9096365cc	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/2a2ac42a-c816-41f4-9328-50fea45c119d.png
1637	38	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	490aa645-8f77-4565-abab-4249379de5b0	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/c986c7e3-9fc2-447a-a417-3029dd5fd72a.png
1637	39	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	905e9b23-147c-4efe-aa99-8ee2adb84b5c	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/f994a870-c32a-4503-ae10-605125b0e8ce.png
1637	40	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	0503ae53-4e61-419c-a2f2-cbdfbef6ff36	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/7a1aac36-84e8-4cd0-92bb-3d9984343a8f.png
1637	41	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	75b8e727-188b-4b80-9ef2-98d668190036	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/2473e8f6-0ce2-4bce-a6e1-b0dd337f71a5.png
1637	42	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	481caa28-3b9d-4a27-8379-94e2dce81463	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/705bef02-d157-434c-80a2-24f80646c06b.png
1637	43	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	06869d74-5eb1-4dcd-916b-7b2724feee6b	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/86c3fcfe-e495-4369-be25-ea12923b065c.png
1637	44	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	ad50f72f-f66d-4c98-ab0a-c2c933721f0c	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/9393a89e-bbf0-42f7-aca5-b3dae1dc9019.png
1637	45	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	0b337a50-2429-40d0-9dec-621d293397ae	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/79815993-97d2-41ed-a3e6-7d83b991c26a.png
1637	46	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	d48f3fa5-6d6c-4262-a4fe-f164b886d427	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/17320f5c-94b2-4a53-b714-f690b88b99ed.png
1637	47	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	f357f4b4-7648-4f7e-895a-0413ed302095	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/1b130a40-085f-4fce-8798-dca7be26634c.png
1637	48	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	b0ea3115-5182-4815-a594-d7432b86c3fa	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/364e572f-f586-4877-895a-812ed312767b.png
1637	49	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	b839fd00-6dce-4804-82cb-60de40b80a39	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/8de5aae1-75a7-4e7e-9964-5e056565f50d.png
1637	50	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	293d3372-9904-4a10-89b2-9d096b05c734	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/c5acf396-f891-4df7-a6ea-6a4f961f8149.png
1637	51	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	529162bf-d3c5-471f-abab-b71498d9318a	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/b882b1a9-9b96-4340-84e5-3f87af810456.png
1637	52	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	e0d7ee25-f681-4717-800b-50e4df644041	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/537e7f4d-f7ab-49f7-b2cd-d5009fab45a1.png
1637	53	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	3830ef72-f3e9-430b-a87d-4601ba080cf6	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/b2f87b48-dad9-48b5-ae34-717c61bc1ac0.png
1637	54	1156	b4905dd5-6c5f-4ccb-b604-21a8f6e7ad72	bec5b507-af62-4c3c-b1d5-52cbaba5abe8	https://pub-08339be418cd41e4a0d0082511a4d701.r2.dev/books/pages/2d0127aa-a54e-4534-ba05-a6bdd8b2036e.png
\.


--
-- Data for Name: book_progress; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_progress (active, completed, current_page, progress_percent, total_pages, created_at, last_read_at, updated_at, book_id, id, student_id, created_by, updated_by) FROM stdin;
\.


--
-- Data for Name: class; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.class (active, created_at, updated_at, id, teacher_id, created_by, name, shift_enum, updated_by) FROM stdin;
\.


--
-- Data for Name: coin; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.coin (id, active, created_at, created_by, updated_at, updated_by, coin, feedback, student_id) FROM stdin;
\.


--
-- Data for Name: feedback; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.feedback (active, rating, created_at, updated_at, id, teacher_id, created_by, title, updated_by) FROM stdin;
\.


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	<< Flyway Baseline >>	BASELINE	<< Flyway Baseline >>	\N	postgres	2026-05-08 16:07:09.974642	0	t
\.


--
-- Data for Name: homework; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.homework (active, deadline, created_at, updated_at, id, lesson_id, created_by, deadline_enum, description, title, updated_by) FROM stdin;
\.


--
-- Data for Name: lesson; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lesson (active, created_at, updated_at, id, subject_id, created_by, description, file_url, name, updated_by) FROM stdin;
t	2026-05-04 19:32:51.438+05	2026-05-04 19:32:51.936+05	3fa85f64-5717-4562-b3fc-2c963f66afa7	3fa85f64-5717-4562-b3fc-2c963f66afa6	\N	qwerty	\N	qwerty	\N
t	2026-05-07 14:12:57.011403+05	2026-05-07 14:12:57.011403+05	53063cc0-e271-47bf-964a-b10fb19f1f68	3fa85f64-5717-4562-b3fc-2c963f66afa6	998900000000	string	string	string	998900000000
t	2026-05-07 14:13:04.725213+05	2026-05-07 14:13:04.725213+05	10a23371-e5ca-4011-ac30-2c57eefb3ffe	3fa85f64-5717-4562-b3fc-2c963f66afa6	998900000000	string	string	string	998900000000
\.


--
-- Data for Name: mark; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.mark (active, active_score, behaviour_score, date, homework_score, total_score, created_at, updated_at, id, student_id, created_by, mark_category_status, updated_by) FROM stdin;
\.


--
-- Data for Name: notification; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.notification (active, is_read, created_at, updated_at, id, parent_id, student_id, created_by, description, message, updated_by) FROM stdin;
t	f	2026-05-06 14:42:16.638056+05	2026-05-06 14:42:16.638056+05	f836ddc4-7773-463e-ac01-c8f9d3bd0db2	13071173-374b-49a9-a4ab-09370b1ec67c	\N	anonymousUser	Siz tizimga muvaffaqiyatli kirdingiz!	Sfera Academy xabarnomasi	anonymousUser
t	f	2026-05-07 14:12:05.85801+05	2026-05-07 14:12:05.85801+05	e36602e9-bcdf-4aab-a77d-89c0952d6b0c	13071173-374b-49a9-a4ab-09370b1ec67c	\N	anonymousUser	Siz tizimga muvaffaqiyatli kirdingiz!	Sfera Academy xabarnomasi	anonymousUser
t	f	2026-05-08 14:46:45.589435+05	2026-05-08 14:46:45.589435+05	c9c339ff-35b1-4132-805e-624b841fddf6	13071173-374b-49a9-a4ab-09370b1ec67c	\N	anonymousUser	Siz tizimga muvaffaqiyatli kirdingiz!	Sfera Academy xabarnomasi	anonymousUser
\.


--
-- Data for Name: page_block; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.page_block (active, completed, height, required, width, x, y, created_at, updated_at, id, page_id, body, created_by, image_url, title, type, updated_by) FROM stdin;
t	\N	10	\N	10	10	10	2026-05-04 14:39:33.23649+05	2026-05-04 14:39:33.23649+05	fc95ac86-b7b8-41fc-b5e6-8378f52e233e	581c382e-58c3-46d1-b857-a2e2d1d9080a	string	998900000000	string	string	INFO	998900000000
\.


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product (id, active, created_at, created_by, updated_at, updated_by, count_coin, description, img_url, name) FROM stdin;
e9ff52c2-51b0-4269-9d8a-a36342701588	t	2026-05-08 14:47:12.18262+05	998900000000	2026-05-08 14:47:12.18262+05	998900000000	1	string	string	string
\.


--
-- Data for Name: student; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.student (active, age, coin, created_at, updated_at, id, parent_id, sinf_id, created_by, first_name, img_url, last_name, password, phone, updated_by) FROM stdin;
\.


--
-- Data for Name: subject; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.subject (active, created_at, updated_at, id, created_by, description, name, updated_by) FROM stdin;
t	2026-05-04 19:32:31.611+05	2026-05-04 19:32:32.369+05	3fa85f64-5717-4562-b3fc-2c963f66afa6	\N	qwerty	qwerty	\N
t	2026-05-06 14:43:56.304474+05	2026-05-06 14:43:56.304474+05	fc058de0-85fb-4f27-8215-b241a0494280	998900000000	string	string	998900000000
\.


--
-- Data for Name: submission; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.submission (active, score, created_at, updated_at, homework_id, id, student_id, created_by, feedback, file_url, text_answer, updated_by) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (active, enabled, created_at, updated_at, id, created_by, first_name, img_url, last_name, password, phone, role, updated_by) FROM stdin;
f	t	2026-05-04 14:31:33.817741+05	2026-05-04 14:31:33.817741+05	13071173-374b-49a9-a4ab-09370b1ec67c	system	Admin	\N	Admin	$2a$10$vNjtwmr4xgiIqoedyGphuufU0OZkA1fZT6j9PhC3J6QTirrDtogHu	998900000000	ROLE_SUPER_ADMIN	system
\.


--
-- Data for Name: video_lesson; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.video_lesson (active, duration, created_at, file_size, updated_at, id, lesson_id, created_by, format, title, updated_by, video_url) FROM stdin;
t	0	2026-05-07 14:13:04.74808+05	0	2026-05-07 14:13:04.74808+05	6c835c89-3774-4a57-adb4-135c6515a5b8	10a23371-e5ca-4011-ac30-2c57eefb3ffe	998900000000	string	string	998900000000	string
t	0	2026-05-07 14:13:58.90018+05	0	2026-05-07 14:13:58.90018+05	da1e7a05-a46e-42a7-b96e-6b6b87f0033f	3fa85f64-5717-4562-b3fc-2c963f66afa7	998900000000	string	string	998900000000	string
\.


--
-- Data for Name: video_progress; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.video_progress (active, completed, watched_seconds, created_at, last_watched_at, updated_at, id, student_id, video_lesson_id, created_by, updated_by, last_position_seconds) FROM stdin;
\.


--
-- Name: attendance attendance_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT attendance_pkey PRIMARY KEY (id);


--
-- Name: block_progress block_progress_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.block_progress
    ADD CONSTRAINT block_progress_pkey PRIMARY KEY (id);


--
-- Name: book_page book_page_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_page
    ADD CONSTRAINT book_page_pkey PRIMARY KEY (id);


--
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);


--
-- Name: book_progress book_progress_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_progress
    ADD CONSTRAINT book_progress_pkey PRIMARY KEY (id);


--
-- Name: class class_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.class
    ADD CONSTRAINT class_pkey PRIMARY KEY (id);


--
-- Name: coin coin_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coin
    ADD CONSTRAINT coin_pkey PRIMARY KEY (id);


--
-- Name: feedback feedback_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.feedback
    ADD CONSTRAINT feedback_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: homework homework_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.homework
    ADD CONSTRAINT homework_pkey PRIMARY KEY (id);


--
-- Name: lesson lesson_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lesson
    ADD CONSTRAINT lesson_pkey PRIMARY KEY (id);


--
-- Name: mark mark_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mark
    ADD CONSTRAINT mark_pkey PRIMARY KEY (id);


--
-- Name: notification notification_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- Name: page_block page_block_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.page_block
    ADD CONSTRAINT page_block_pkey PRIMARY KEY (id);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- Name: student student_phone_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_phone_key UNIQUE (phone);


--
-- Name: student student_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_pkey PRIMARY KEY (id);


--
-- Name: subject subject_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subject
    ADD CONSTRAINT subject_pkey PRIMARY KEY (id);


--
-- Name: submission submission_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.submission
    ADD CONSTRAINT submission_pkey PRIMARY KEY (id);


--
-- Name: video_progress ukem3429vep9mikynodnon2dtt8; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.video_progress
    ADD CONSTRAINT ukem3429vep9mikynodnon2dtt8 UNIQUE (student_id, video_lesson_id);


--
-- Name: users users_phone_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_phone_key UNIQUE (phone);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: video_lesson video_lesson_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.video_lesson
    ADD CONSTRAINT video_lesson_pkey PRIMARY KEY (id);


--
-- Name: video_progress video_progress_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.video_progress
    ADD CONSTRAINT video_progress_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: homework fk1hfa7auounxtrsgqvp694ixhf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.homework
    ADD CONSTRAINT fk1hfa7auounxtrsgqvp694ixhf FOREIGN KEY (lesson_id) REFERENCES public.lesson(id);


--
-- Name: block_progress fk22jwbxuc6un2l005w46r6aiy2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.block_progress
    ADD CONSTRAINT fk22jwbxuc6un2l005w46r6aiy2 FOREIGN KEY (block_id) REFERENCES public.page_block(id);


--
-- Name: class fk34v5b06opg9o8muvajovprk37; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.class
    ADD CONSTRAINT fk34v5b06opg9o8muvajovprk37 FOREIGN KEY (teacher_id) REFERENCES public.users(id);


--
-- Name: book_progress fk59wbhdd5s0yqgejmhctbtthpd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_progress
    ADD CONSTRAINT fk59wbhdd5s0yqgejmhctbtthpd FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: feedback fk72u2796sg5nmrvvboi6eqgam7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.feedback
    ADD CONSTRAINT fk72u2796sg5nmrvvboi6eqgam7 FOREIGN KEY (teacher_id) REFERENCES public.users(id);


--
-- Name: book fk7ql5tfbkjvbc3k9xhtfva8nxq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT fk7ql5tfbkjvbc3k9xhtfva8nxq FOREIGN KEY (subject_id) REFERENCES public.subject(id);


--
-- Name: lesson fk7ydr23s8y9j6lip5qrngoymx4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lesson
    ADD CONSTRAINT fk7ydr23s8y9j6lip5qrngoymx4 FOREIGN KEY (subject_id) REFERENCES public.subject(id);


--
-- Name: video_progress fkb2f989itebxnh3xa26j5au7q7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.video_progress
    ADD CONSTRAINT fkb2f989itebxnh3xa26j5au7q7 FOREIGN KEY (video_lesson_id) REFERENCES public.video_lesson(id);


--
-- Name: book_progress fkc3y1hf986uj5do0ihhy9r71yi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_progress
    ADD CONSTRAINT fkc3y1hf986uj5do0ihhy9r71yi FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- Name: page_block fkc8mrqb71fmbsomv86ukb48duq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.page_block
    ADD CONSTRAINT fkc8mrqb71fmbsomv86ukb48duq FOREIGN KEY (page_id) REFERENCES public.book_page(id);


--
-- Name: mark fkcwocngy0rfmqdhqwm3qlrfamx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mark
    ADD CONSTRAINT fkcwocngy0rfmqdhqwm3qlrfamx FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: attendance fkdhdkmk36otyeoibk1xkgsv5s0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT fkdhdkmk36otyeoibk1xkgsv5s0 FOREIGN KEY (sinf_id) REFERENCES public.class(id);


--
-- Name: notification fkeriefbtifpjsvo8qifc8rl0vf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT fkeriefbtifpjsvo8qifc8rl0vf FOREIGN KEY (parent_id) REFERENCES public.users(id);


--
-- Name: coin fkevop1o3yxmgved48utinl6nt5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.coin
    ADD CONSTRAINT fkevop1o3yxmgved48utinl6nt5 FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: book fkg31io0jwns0vnl2c90l250fnp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT fkg31io0jwns0vnl2c90l250fnp FOREIGN KEY (lesson_id) REFERENCES public.lesson(id);


--
-- Name: student fkglumqgokr3kfaw3ehpjo2ptxb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT fkglumqgokr3kfaw3ehpjo2ptxb FOREIGN KEY (parent_id) REFERENCES public.users(id);


--
-- Name: video_progress fkgpjk3xeakfnfp938amv5w4xok; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.video_progress
    ADD CONSTRAINT fkgpjk3xeakfnfp938amv5w4xok FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: submission fkhncywuw9vwff2htaofx9m3m75; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.submission
    ADD CONSTRAINT fkhncywuw9vwff2htaofx9m3m75 FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: video_lesson fkit1assc4y184uhpx7g75sj95d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.video_lesson
    ADD CONSTRAINT fkit1assc4y184uhpx7g75sj95d FOREIGN KEY (lesson_id) REFERENCES public.lesson(id);


--
-- Name: submission fkkry4vgv9iivvb4phtn1qeu9h4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.submission
    ADD CONSTRAINT fkkry4vgv9iivvb4phtn1qeu9h4 FOREIGN KEY (homework_id) REFERENCES public.homework(id);


--
-- Name: attendance fknq6vm31it076obtjf2qp5coim; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT fknq6vm31it076obtjf2qp5coim FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: notification fkopnvuweg49llrxcdnh7k47kt9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT fkopnvuweg49llrxcdnh7k47kt9 FOREIGN KEY (student_id) REFERENCES public.student(id);


--
-- Name: student fkrw55m391rtg7qlpsr966e51o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT fkrw55m391rtg7qlpsr966e51o FOREIGN KEY (sinf_id) REFERENCES public.class(id);


--
-- Name: book_page fks20ajladoyi0gay1t6dtosfue; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_page
    ADD CONSTRAINT fks20ajladoyi0gay1t6dtosfue FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- PostgreSQL database dump complete
--

\unrestrict dhm57cTEN8cch2dt3CZW4dXPuRsWB1DzCy5ictP0EosHtwe01OzBUzsrd9lPG3c

