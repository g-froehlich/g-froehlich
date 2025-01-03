The database is under the database folder and my postman collection is under the postman folder.

(I wrote these in order of hierarchy: admin can do all, then league managers, then players)
This RESTful API allows users to do the following:

As an authenticated admin I can:
- Create a court.
- Update a court.
- Delete a court.

As an authenticated league manager I can:
- Create a league.
- Update a league.
- Delete a league.

//bugged, worked but after a deletion attempt it broke
- Create a player.
- Update a player.
- Delete a player.

//bugged, worked but after a deletion attempt it broke
- Create a team.
- Update a team.
- Delete a team.

As an unauthenticated user/player I can:
- Review all courts available in the database.
- Review courts by a specific id.
- Review courts by name.
- Review courts by address.

- Review all leagues in the database.
- Review leagues by a specific id.
- Review all leagues managed by a specific league manager.
- Review leagues by name.

- Review all player matches.
- Review player matches by a specific id.
- Review all player matches attached to a league.
- Review all player matches attached to the first player.
- Review all player matches attached to the second player.
- Create a player match.
- Update a player match.
- Delete a player match.

- Review all team matches.
- Review team matches by a specific id.
- Review all team matches attached to a league.
- Review all team matches attached to the first team.
- Review all team matches attached to the second team.
- Create a team match.
- Update a team match.
- Delete a team match.

- Review all players in the database.
- Review a player by specific id.
- Review a player by their first and last name.

- Review all teams in the database.
- Review a team by specific id.
- Review a team by their team name.

