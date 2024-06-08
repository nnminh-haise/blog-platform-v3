-- Table: public.blogs

-- DROP TABLE IF EXISTS public.blogs;

CREATE TABLE IF NOT EXISTS public.blogs
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    title text COLLATE pg_catalog."default" NOT NULL,
    description text COLLATE pg_catalog."default",
    attachment text COLLATE pg_catalog."default" NOT NULL,
    slug text COLLATE pg_catalog."default" NOT NULL,
    create_at timestamp without time zone NOT NULL,
    update_at timestamp without time zone NOT NULL,
    delete_at timestamp without time zone,
    thumbnail text COLLATE pg_catalog."default",
    sub_title text COLLATE pg_catalog."default",
    is_popular boolean DEFAULT false,
    CONSTRAINT blogs_pkey PRIMARY KEY (id),
    CONSTRAINT "unique-slug" UNIQUE (slug),
    CONSTRAINT "unique-title" UNIQUE (title),
    CONSTRAINT "valid-create-and-update-timestamp" CHECK (create_at <= update_at)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.blogs
    OWNER to postgres;