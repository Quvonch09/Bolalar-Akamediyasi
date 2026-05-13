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
                                   CONSTRAINT attendance_status_check CHECK (
                                       ((status)::text = ANY (
                                           (ARRAY[
                                               'KELDI',
                                               'KELMADI',
                                               'SABABLI'
                                               ])::text[]
                                           ))
                                       )
);

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
                             CONSTRAINT book_status_check CHECK (
                                 ((status)::text = ANY (
                                     (ARRAY[
                                         'PROCESSING',
                                         'READY',
                                         'FAILED'
                                         ])::text[]
                                     ))
                                 )
);

CREATE TABLE public.book_page (
                                  height integer,
                                  page_number integer,
                                  width integer,
                                  book_id uuid,
                                  id uuid NOT NULL,
                                  image_url character varying(255)
);

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

-- qolgan CREATE TABLE lar ham xuddi shu tarzda davom etadi


ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT attendance_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.block_progress
    ADD CONSTRAINT block_progress_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.book_page
    ADD CONSTRAINT book_page_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.book_progress
    ADD CONSTRAINT book_progress_pkey PRIMARY KEY (id);

-- boshqa PRIMARY KEY va UNIQUE constraintlar


ALTER TABLE ONLY public.homework
    ADD CONSTRAINT fk1hfa7auounxtrsgqvp694ixhf
        FOREIGN KEY (lesson_id)
            REFERENCES public.lesson(id);

ALTER TABLE ONLY public.block_progress
    ADD CONSTRAINT fk22jwbxuc6un2l005w46r6aiy2
        FOREIGN KEY (block_id)
            REFERENCES public.page_block(id);

ALTER TABLE ONLY public.class
    ADD CONSTRAINT fk34v5b06opg9o8muvajovprk37
        FOREIGN KEY (teacher_id)
            REFERENCES public.users(id);

-- barcha FOREIGN KEY lar ham shu tarzda qoladi