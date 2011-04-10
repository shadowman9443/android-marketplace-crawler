class CreateTargets < ActiveRecord::Migration
  def self.up
    create_table :targets do |t|
      t.string :name

      t.timestamps
    end
    add_index :targets, :name, :unique => true    
  end

  def self.down
    drop_table :targets
  end
end
