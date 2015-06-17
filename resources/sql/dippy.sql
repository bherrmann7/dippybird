
-- name: all-entries
SELECT * FROM entry order by date_created desc

-- name: fetch-entry
select * from entry where id = :id

-- name: insert-new-post!
--                        id, version,body,date_created,permgen, title
insert into entry values (null, 1, :body, :date, 'notset', :title)

-- name: update-post!
update entry set date_created=:date, title=:title, body=:body where id = :id

-- name: delete-post!
delete from entry where id=?
