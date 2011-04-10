class CreateRatings < ActiveRecord::Migration
  def self.up
    create_table :ratings do |t|
      t.string :rating
      t.integer :ratingCount
      t.integer :downloadCount
      t.string :downloadCountText
      t.references :app

      t.timestamps
    end
    add_index :ratings, :app_id, :unique => true 
  end

  def self.down
    drop_table :ratings
  end
end
