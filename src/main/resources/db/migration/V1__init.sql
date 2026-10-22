--
-- PostgreSQL database dump
--


-- Dumped from database version 17.11 (Debian 17.11-1.pgdg13+2)
-- Dumped by pg_dump version 17.11 (Debian 17.11-1.pgdg13+2)




--
-- Name: card_subtypes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.card_subtypes (
    card_id character varying(255) NOT NULL,
    subtype character varying(255) NOT NULL,
    "position" integer NOT NULL,
    CONSTRAINT card_subtypes_position_check CHECK (("position" >= 0))
);


--
-- Name: card_types; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.card_types (
    card_id character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    "position" integer NOT NULL,
    CONSTRAINT card_types_position_check CHECK (("position" >= 0))
);


--
-- Name: cards; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cards (
    id character varying(255) NOT NULL,
    abilities jsonb,
    ancient_trait jsonb,
    artist character varying(255),
    attacks jsonb,
    cardmarket jsonb,
    converted_retreat_cost integer,
    evolves_from character varying(255),
    evolves_to jsonb,
    flavor_text text,
    hp character varying(255),
    images jsonb,
    legalities jsonb,
    level character varying(255),
    name character varying(255) NOT NULL,
    national_pokedex_numbers jsonb,
    number character varying(255) NOT NULL,
    rarity character varying(255),
    regulation_mark character varying(255),
    resistances jsonb,
    retreat_cost jsonb,
    rules jsonb,
    supertype character varying(255) NOT NULL,
    tcgplayer jsonb,
    weaknesses jsonb,
    set_id character varying(255) NOT NULL
);


--
-- Name: sets; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sets (
    id character varying(255) NOT NULL,
    images jsonb,
    legalities jsonb,
    name character varying(255) NOT NULL,
    printed_total integer NOT NULL,
    ptcgo_code character varying(255),
    release_date character varying(255) NOT NULL,
    series character varying(255) NOT NULL,
    total integer NOT NULL,
    updated_at character varying(255) NOT NULL
);


--
-- Name: card_subtypes card_subtypes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.card_subtypes
    ADD CONSTRAINT card_subtypes_pkey PRIMARY KEY (card_id, "position");


--
-- Name: card_types card_types_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.card_types
    ADD CONSTRAINT card_types_pkey PRIMARY KEY (card_id, "position");


--
-- Name: cards cards_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cards
    ADD CONSTRAINT cards_pkey PRIMARY KEY (id);


--
-- Name: sets sets_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sets
    ADD CONSTRAINT sets_pkey PRIMARY KEY (id);


--
-- Name: idx_card_subtypes_subtype; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_card_subtypes_subtype ON public.card_subtypes USING btree (subtype);


--
-- Name: idx_card_types_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_card_types_type ON public.card_types USING btree (type);


--
-- Name: idx_cards_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_cards_name ON public.cards USING btree (name);


--
-- Name: idx_set_release_date; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_set_release_date ON public.sets USING btree (release_date);


--
-- Name: cards fkel3vwert8xhu7x61qqaoiijuj; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cards
    ADD CONSTRAINT fkel3vwert8xhu7x61qqaoiijuj FOREIGN KEY (set_id) REFERENCES public.sets(id);


--
-- Name: card_subtypes fkihgh86metcwui0xiwh9qtctha; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.card_subtypes
    ADD CONSTRAINT fkihgh86metcwui0xiwh9qtctha FOREIGN KEY (card_id) REFERENCES public.cards(id);


--
-- Name: card_types fkosrqd0tddqcwt1oqp2oy7l0rx; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.card_types
    ADD CONSTRAINT fkosrqd0tddqcwt1oqp2oy7l0rx FOREIGN KEY (card_id) REFERENCES public.cards(id);


--
-- PostgreSQL database dump complete
--


