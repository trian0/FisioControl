-- FisioControl — Supabase Schema
-- Execute este script no SQL Editor do Supabase (Settings > SQL Editor)

-- =====================
-- PROFILES (linked to auth.users)
-- =====================
create table public.profiles (
    id          uuid primary key references auth.users(id) on delete cascade,
    full_name   text not null,
    role        text not null default 'physio',
    avatar_url  text,
    created_at  timestamptz not null default now()
);

alter table public.profiles enable row level security;
create policy "Users can read own profile"   on public.profiles for select using (auth.uid() = id);
create policy "Users can update own profile" on public.profiles for update using (auth.uid() = id);

-- Auto-create profile on signup
create or replace function public.handle_new_user()
returns trigger language plpgsql security definer set search_path = public as $$
begin
  insert into public.profiles (id, full_name)
  values (new.id, coalesce(new.raw_user_meta_data->>'full_name', new.email));
  return new;
end;
$$;
create trigger on_auth_user_created
  after insert on auth.users
  for each row execute procedure public.handle_new_user();

-- =====================
-- PLAYERS
-- =====================
create table public.players (
    id          uuid primary key default gen_random_uuid(),
    full_name   text not null,
    birth_date  date,
    position    text,
    team        text,
    phone       text,
    photo_url   text,
    notes       text,
    is_active   boolean not null default true,
    created_by  uuid references public.profiles(id),
    created_at  timestamptz not null default now(),
    updated_at  timestamptz not null default now()
);

alter table public.players enable row level security;
create policy "Authenticated read players"   on public.players for select  using (auth.role() = 'authenticated');
create policy "Authenticated insert players" on public.players for insert  with check (auth.role() = 'authenticated');
create policy "Authenticated update players" on public.players for update  using (auth.role() = 'authenticated');

create index idx_players_is_active  on public.players(is_active);
create index idx_players_created_by on public.players(created_by);

-- =====================
-- TREATMENT SCHEDULES
-- =====================
create table public.treatment_schedules (
    id                  uuid primary key default gen_random_uuid(),
    player_id           uuid not null references public.players(id) on delete cascade,
    title               text not null,
    description         text,
    start_date          date not null,
    end_date            date,
    status              text not null default 'active',
    exercises           jsonb not null default '[]',
    sessions_per_week   int not null default 3,
    created_by          uuid references public.profiles(id),
    created_at          timestamptz not null default now(),
    updated_at          timestamptz not null default now()
);

alter table public.treatment_schedules enable row level security;
create policy "Authenticated manage schedules" on public.treatment_schedules
    for all using (auth.role() = 'authenticated');

create index idx_schedules_player_id on public.treatment_schedules(player_id);
create index idx_schedules_status    on public.treatment_schedules(status);

-- =====================
-- DAILY EVOLUTIONS
-- =====================
create table public.daily_evolutions (
    id                  uuid primary key default gen_random_uuid(),
    player_id           uuid not null references public.players(id) on delete cascade,
    schedule_id         uuid references public.treatment_schedules(id),
    session_date        date not null default current_date,
    pain_scale          int check (pain_scale between 0 and 10),
    physiotherapy_procedures text,
    objective_note      text,
    recorded_by         uuid references public.profiles(id),
    created_at          timestamptz not null default now()
);

alter table public.daily_evolutions enable row level security;
create policy "Authenticated manage evolutions" on public.daily_evolutions
    for all using (auth.role() = 'authenticated');

create index idx_evolutions_player_id on public.daily_evolutions(player_id);
create index idx_evolutions_date      on public.daily_evolutions(session_date);

-- =====================
-- AUTO updated_at TRIGGERS
-- =====================
create or replace function update_updated_at()
returns trigger language plpgsql as $$
begin new.updated_at = now(); return new; end;
$$;

create trigger players_updated_at
    before update on public.players
    for each row execute procedure update_updated_at();

create trigger schedules_updated_at
    before update on public.treatment_schedules
    for each row execute procedure update_updated_at();
