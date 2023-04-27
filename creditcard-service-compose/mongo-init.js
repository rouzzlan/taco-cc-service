db.createUser(
    {
        user: "rouslan",
        pwd: "50m9FiD3",
        roles: [
            {
                role: "readWrite",
                db: "CCServiceDB"
            }
        ]
    }
);

db = new Mongo().getDB('CCServiceDB');

db.createCollection('CreditCards');