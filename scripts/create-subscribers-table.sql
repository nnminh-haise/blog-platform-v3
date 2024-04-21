-- Table: public.subscribers

-- DROP TABLE IF EXISTS public.subscribers;

CREATE TABLE IF NOT EXISTS public.subscribers
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    email text COLLATE pg_catalog."default" NOT NULL,
    full_name text COLLATE pg_catalog."default" NOT NULL,
    create_at timestamp without time zone NOT NULL,
    update_at timestamp without time zone NOT NULL,
    delete_at timestamp without time zone,
    CONSTRAINT subscribers_pkey PRIMARY KEY (id),
    CONSTRAINT "Unique subscriber email" UNIQUE (email),
    CONSTRAINT "valid-create-and-update-timestamp" CHECK (create_at <= update_at)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.subscribers
    OWNER to postgres;