insert into public.users (id, username, password, role)
values
    ('1', 'user', '$2a$10$ABkpfVaUh6jDcYj3s2anLelGQH9tBbgrmgFN1XA3rzP3ZeLvlj8gy', 'USER'),
    ('2', 'admin', '$2a$10$ABkpfVaUh6jDcYj3s2anLelGQH9tBbgrmgFN1XA3rzP3ZeLvlj8gy', 'ADMIN');

select setval('public.users_id_seq', (select max(id) from public.users));

insert into public.places (id, name)
values
    (1, 'place1'),
    (2, 'place2');

select setval('public.places_id_seq', (select max(id) from public.places));